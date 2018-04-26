package com.thesamet.intellij

import com.intellij.openapi.actionSystem._
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.AlignSingleLineCaseStatements.MaxArrowIndent
import scalariform.formatter.preferences._

case class FileDocument(file: VirtualFile, document: Document) {
  def isScala: Boolean = true //file.getFileType.getName == "Scala"
}

class ScalariformFormatAction extends AnAction {

  /*def loadPreferences(path: String): Unit = {
    val prefs = PreferencesImporterExporter.loadPreferences(path)
    val state = ServiceManager.getService(classOf[ScalariformState])
    def castedGet[T](descriptor: PreferenceDescriptor[T]) = prefs.preferencesMap.get(descriptor).asInstanceOf[Option[T]]
    castedGet(AlignArguments).foreach(state.setAlignArguments)
    castedGet(AlignParameters).foreach(state.setAlignParameters)
    castedGet(AlignSingleLineCaseStatements).foreach(state.setAlignSingleLineCase)
    castedGet(AlignSingleLineCaseStatements.MaxArrowIndent).foreach(state.setAlignSingleLineCaseStatementsMaxArrowIndent(_))
    castedGet(IndentSpaces).foreach(state.setIndentSpaces(_))
    castedGet(SpaceBeforeColon).foreach(state.setSpaceBeforeColon)
    castedGet(CompactStringConcatenation).foreach(state.setCompactStringConcatenation)
    castedGet(PreserveSpaceBeforeArguments).foreach(state.setPreserveSpaceBeforeArguments)
    castedGet(AlignParameters).foreach(state.setAlignParameters)
    castedGet(DoubleIndentConstructorArguments).foreach(state.setDoubleIndentClassDeclaration)
    castedGet(FormatXml).foreach(state.setFormatXML)
    castedGet(IndentPackageBlocks).foreach(state.setIndentPackageBlocks)
    castedGet(AlignSingleLineCaseStatements).foreach(state.setAlignSingleLineCase)
    castedGet(MaxArrowIndent).foreach(state.setAlignSingleLineCaseStatementsMaxArrowIndent(_))
    castedGet(IndentLocalDefs).foreach(state.setIndentLocalDefs)
    castedGet(SpaceInsideParentheses).foreach(state.setSpaceInsideParenthesis)
    castedGet(SpaceInsideBrackets).foreach(state.setSpaceInsideBrackets)
    castedGet(SpacesWithinPatternBinders).foreach(state.setSpacesWithinPatternBinders)
    castedGet(MultilineScaladocCommentsStartOnFirstLine).foreach(state.setMultilineScalaDocCommentsStartOnFirstLine)
    castedGet(IndentWithTabs).foreach(state.setIndentWithTabs)
    castedGet(CompactControlReadability).foreach(state.setCompactControlReadability)
    castedGet(PlaceScaladocAsterisksBeneathSecondAsterisk).foreach(state.setPlaceScalaDocAsteriskBeneathSecondAsterisk)
    castedGet(DoubleIndentMethodDeclaration).foreach(state.setDoubleIndentMethodDeclaration)
    castedGet(AlignArguments).foreach(state.setAlignArguments)
    castedGet(SpacesAroundMultiImports).foreach(state.setSpacesAroundMultiImports)
    castedGet(DanglingCloseParenthesis).foreach(i => state.setDanglingCloseParenthesis(i.toString))
  }*/

  override def update(event: AnActionEvent): Unit = {
    event.getPresentation.setEnabled(getCurrentFileDocument(event).exists(_.isScala))
  }

  override def actionPerformed(event: AnActionEvent) {
    lazy val pref = formattingPreferences
    getCurrentFileDocument(event)
      .filterNot(_.file.isDirectory)
      .filter(_.isScala)
      .foreach {
        fileDoc =>
          val source = fileDoc.document.getText()
          val formatted = ScalaFormatter.format(source, formattingPreferences = pref)
          if (source != formatted) {
            val project = event.getData(CommonDataKeys.PROJECT)
            WriteCommandAction.runWriteCommandAction(project, "Scalariform", null, new Runnable {
              override def run(): Unit = fileDoc.document.setText(formatted)
            })
          }
      }
  }

  private def getCurrentFileDocument(event: AnActionEvent): Seq[FileDocument] = for {
    vdir <- event.getDataContext.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY).toSeq
    vfile <- if(vdir.isDirectory) vdir.getChildren else Array(vdir)
  } yield FileDocument(vfile, FileDocumentManager.getInstance().getDocument(vfile))

  private def formattingPreferences: FormattingPreferences = {
    val component: ScalariformState = ServiceManager.getService(classOf[ScalariformState])

    FormattingPreferences.setPreference(RewriteArrowSymbols, component.isRewriteArrowSymbols)
      .setPreference(IndentSpaces, component.getIndentSpaces.toInt)
      .setPreference(SpaceBeforeColon, component.isSpaceBeforeColon)
      .setPreference(CompactStringConcatenation, component.isCompactStringConcatenation)
      .setPreference(PreserveSpaceBeforeArguments, component.isPreserveSpaceBeforeArguments)
      .setPreference(AlignParameters, component.isAlignParameters)
      .setPreference(DoubleIndentConstructorArguments, component.isDoubleIndentClassDeclaration)
      .setPreference(FormatXml, component.isFormatXML)
      .setPreference(IndentPackageBlocks, component.isIndentPackageBlocks)
      .setPreference(AlignSingleLineCaseStatements, component.isAlignSingleLineCase)
      .setPreference(MaxArrowIndent, component.getAlignSingleLineCaseStatementsMaxArrowIndent.toInt)
      .setPreference(IndentLocalDefs, component.isIndentLocalDefs)
      //.setPreference(PreserveDanglingCloseParenthesis, component.isPreserveDanglineCloseParenthesis)
      .setPreference(SpaceInsideParentheses, component.isSpaceInsideParenthesis)
      .setPreference(SpaceInsideBrackets, component.isSpaceInsideBrackets)
      .setPreference(SpacesWithinPatternBinders, component.isSpacesWithinPatternBinders)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, component.isMultilineScalaDocCommentsStartOnFirstLine)
      .setPreference(IndentWithTabs, component.isIndentWithTabs)
      .setPreference(CompactControlReadability, component.isCompactControlReadability)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, component.isPlaceScalaDocAsteriskBeneathSecondAsterisk)
      .setPreference(DoubleIndentMethodDeclaration, component.isDoubleIndentMethodDeclaration)
      .setPreference(AlignArguments, component.isAlignArguments)
      .setPreference(SpacesAroundMultiImports, component.isSpacesAroundMultiImports)
      .setPreference(DanglingCloseParenthesis, IntentPreference.parseValue(component.getDanglingCloseParenthesis).right.get)
  }

}
