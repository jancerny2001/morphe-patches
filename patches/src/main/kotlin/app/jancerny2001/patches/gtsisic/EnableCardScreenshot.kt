package app.jancerny2001.patches.gtsisic

import app.morphe.patcher.Compatibility
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.clearBody
import app.morphe.patcher.patch.bytecodePatch

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
        method.clearBody()
        method.addInstructions(
            0,
            """
                iget-object v0, p0, Lm69;->l0:Landroid/view/Window;
                const/16 v1, 0x2000
                invoke-virtual {v0, v1}, Landroid/view/Window;->clearFlags(I)V
                sget-object v0, Liu8;->a:Liu8;
                return-object v0
            """
        )
    }
}