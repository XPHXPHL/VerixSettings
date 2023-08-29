package com.xlp.verixsettings.utils

object Init {
    var name:String = ""
    const val TAG: String = "XLP_DEBUG"
    val batteryDesign =
        execShell("cat /sys/class/power_supply/battery/charge_full_design | awk '{print int(\$1/1000)}'").trim()
    val batteryFull =
        execShell("cat /sys/class/power_supply/battery/charge_full | awk '{print int(\$1/1000)}'").trim()
    val kernelVersion = execShell("cat /proc/version").trim()
    private val socChip = execShell("cat /sys/devices/soc0/chip_name").trim()
    val formatSocChip = formatSocName(socChip)
    private val batteryHealth = batteryFull.toFloat() / batteryDesign.toFloat()
    val formatBatteryHealth = String.format("%.2f%%", batteryHealth * 100)
    val romVersion = getProp("ro.xlp.rom.version.helper")
    val cameraParameter = getProp("ro.xlp.rom.camera.helper")
}