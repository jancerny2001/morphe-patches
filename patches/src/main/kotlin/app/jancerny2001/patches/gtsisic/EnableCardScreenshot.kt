package app.jancerny2001.patches.gtsisic

import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val enableCardScreenshotPatch = bytecodePatch(
    name = "Enable card screenshot",
    description = "Always clears FLAG_SECURE to allow screenshots on all screens."
) {
    compatibleWith(
        Compatibility(
            name = "GTSISIC",
            packageName = "com.bootiq2.gtsisic"
        )
    )

    execute {
        val method = SecureFlagMethodFingerprint.method ?: return@execute
        // Find and replace the addFlags(8192) instruction with clearFlags(8192)
        val instructions = method.instructions
        val addFlagsIndex = instructions.indexOfFirst { instruction ->
            instruction.opcode == Opcode.INVOKE_VIRTUAL &&
            instruction is ReferenceInstruction &&
            (instruction.reference as? MethodReference)?.let {
                it.name == "addFlags" && it.definingClass == "Landroid/view/Window;"
            } == true
        }
        
        if (addFlagsIndex >= 0) {
            method.replaceInstruction(
                addFlagsIndex,
                "invoke-virtual {v0, v1}, Landroid/view/Window;->clearFlags(I)V"
            )
        }
    }
}