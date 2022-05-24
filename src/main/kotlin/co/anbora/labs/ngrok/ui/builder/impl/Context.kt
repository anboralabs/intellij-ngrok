package co.anbora.labs.ngrok.ui.builder.impl

import com.intellij.ui.dsl.builder.impl.ButtonsGroupImpl

class Context {

    private val allButtonsGroups = mutableListOf<ButtonsGroupImpl>()
    private val buttonsGroupsStack = mutableListOf<ButtonsGroupImpl>()

    fun addButtonsGroup(buttonsGroup: ButtonsGroupImpl) {
        allButtonsGroups += buttonsGroup
        buttonsGroupsStack += buttonsGroup
    }

    fun getButtonsGroup(): ButtonsGroupImpl? {
        return buttonsGroupsStack.lastOrNull()
    }

    fun removeLastButtonsGroup() {
        buttonsGroupsStack.removeLast()
    }

    fun postInit() {
        for (buttonsGroup in allButtonsGroups) {
            buttonsGroup.postInit()
        }
    }
}