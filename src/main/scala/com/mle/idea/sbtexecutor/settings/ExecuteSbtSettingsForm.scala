package com.mle.idea.sbtexecutor.settings

import com.intellij.ui.{AnActionButton, AnActionButtonRunnable, CollectionListModel, ToolbarDecorator}
import com.intellij.openapi.ui.LabeledComponent
import javax.swing._
import com.intellij.ui.components.{JBPanel, JBLabel, JBList}
import java.awt._

/**
 *
 * @author mle
 */
class ExecuteSbtSettingsForm {
  // this piece of shit does not extend ListModel[T] but only ListModel
  private val listModel = new CollectionListModel[String]("compile", "clean")
  private val vmOptionsLabel = new JBLabel("VM parameters", SwingConstants.LEFT)
  private val vmOptionsText = new JTextField
  vmOptionsLabel setLabelFor vmOptionsText

  def commands = listModel.getItems

  def vmOptions = vmOptionsText.getText

  def setModel(settings: ExecuteSbtSettings) {
    listModel.removeAll()
    settings.commands foreach (c => listModel add c)
    vmOptionsText setText settings.vmOptions
  }

  private val sbtCommandList: JList[String] = new JBList(listModel).asInstanceOf[JList[String]]
  private val decorator = ToolbarDecorator createDecorator sbtCommandList
  decorator.setEditAction(new AnActionButtonRunnable {
    def run(e: AnActionButton) {
      // show dialog, get name
      val newValue = JOptionPane.showInputDialog("Edit SBT command", sbtCommandList.getSelectedValue)
      if (newValue != null) {
        listModel.setElementAt(newValue, sbtCommandList.getSelectedIndex)
      }
    }
  })
  decorator.setAddAction(new AnActionButtonRunnable {
    def run(e: AnActionButton) {
      // show dialog, get name
      val addedCommand = JOptionPane.showInputDialog("Add SBT command")
      listModel add addedCommand
    }
  })
  private val panel = decorator.createPanel()
  panel setPreferredSize new Dimension(panel.getPreferredSize.getWidth.toInt, 250)
  private val commandsPanel = LabeledComponent.create(panel, "SBT commands in menu")

  val contentPane = new JBPanel()
  contentPane setLayout new BoxLayout(contentPane, BoxLayout.Y_AXIS)
  contentPane add commandsPanel
  contentPane add Box.createRigidArea(new Dimension(0, 10))
  contentPane add vmOptionsLabel
  contentPane add vmOptionsText
  alignLeft(commandsPanel, vmOptionsLabel, vmOptionsText)

  val comp = new JBPanel(new BorderLayout())
  comp add(contentPane, BorderLayout.NORTH)

  def alignLeft(comps: JComponent*) {
    comps foreach (_ setAlignmentX Component.LEFT_ALIGNMENT)
  }
}
