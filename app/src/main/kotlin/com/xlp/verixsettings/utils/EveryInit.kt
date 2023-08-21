package com.xlp.verixsettings.utils

object Init {
    const val TAG: String = "XLP_DEBUG"
    val batteryDesign = execShell("cat /sys/class/power_supply/battery/charge_full_design | awk '{print int(\$1/1000)}'").trim()
    val batteryFull = execShell("cat /sys/class/power_supply/battery/charge_full | awk '{print int(\$1/1000)}'").trim()
    val kernelVersion = execShell("cat /proc/version")
    val socChip = execShell("cat /sys/devices/soc0/chip_name")
    private val batteryHealth = batteryFull.toFloat() / batteryDesign.toFloat()
    val formatBatteryHealth = String.format("%.2f%%", batteryHealth * 100)
}