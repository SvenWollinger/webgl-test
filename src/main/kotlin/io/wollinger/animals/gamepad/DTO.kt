package io.wollinger.animals.gamepad

import kotlinx.browser.window

data class GamepadButton(
    val pressed: Boolean,
    val touched: Boolean,
    val value: Double
)

data class Gamepad(
    val connected: Boolean,
    val id: String,
    val index: Int,
    val mapping: String,
    val buttons: List<GamepadButton>,
    val axes: List<Double>
)

data class GoodGamepad(
    val lStick: Stick,
    val rStick: Stick,
    val l1: Boolean,
    val l2: Bumper,
    val r1: Boolean,
    val r2: Bumper,
    val dpad: ButtonGroup,
    val faceButtons: ButtonGroup,
    val start: Boolean
)

data class ButtonGroup(
    val up: Boolean,
    val down: Boolean,
    val left: Boolean,
    val right: Boolean,
)

data class Bumper(
    val value: Double,
    val pressed: Boolean
)

data class Stick(
    val x: Double,
    val y: Double
)

private fun getInternalGamepad(): Gamepad? {
    val data = (window.navigator.asDynamic().getGamepads() as Array<dynamic>).firstOrNull() ?: return null
    return Gamepad(
        connected = data.connected as Boolean,
        id = data.id as String,
        index = data.index as Int,
        mapping = data.mapping as String,
        axes = (data.axes as Array<dynamic>).map { it as Double },
        buttons = (data.buttons as Array<dynamic>).map {
            GamepadButton(
                pressed = it.pressed as Boolean,
                touched = it.touched as Boolean,
                value = it.value as Double
            )
        }
    )
}

fun getFirstGamepad(): GoodGamepad? {
    val gamepad = getInternalGamepad() ?: return null
    println(gamepad.buttons.first())
    return GoodGamepad(
        lStick = Stick(gamepad.axes[0], gamepad.axes[1]),
        rStick = Stick(gamepad.axes[2], gamepad.axes[3]),
        l1 = false,
        l2 = Bumper(0.0, false),
        r1 = false,
        r2 = Bumper(0.0, false),
        start = false,
        dpad = ButtonGroup(false, false, false, false),
        faceButtons = ButtonGroup(false, false, false, false)
    )
}