package app.template.patches.gtsisic

import app.morphe.patcher.core.Compatibility
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
        // Najdeme metodu pomocí fingerprintu
        val method = SecureFlagMethodFingerprint.method ?: return@execute

        // Smažeme celé původní tělo metody
        method.clearBody()

        // Vložíme nové instrukce, které vždy provedou clearFlags(8192)
        method.addInstructions(
            0,
            """
                # Získáme okno z proměnné this.l0
                iget-object v0, p0, Lm69;->l0:Landroid/view/Window;
                
                # Konstanta FLAG_SECURE = 8192 = 0x2000
                const/16 v1, 0x2000
                
                # Zavoláme window.clearFlags(FLAG_SECURE)
                invoke-virtual {v0, v1}, Landroid/view/Window;->clearFlags(I)V
                
                # Vrátíme lu8.a (původní návratová hodnota)
                sget-object v0, Liu8;->a:Liu8;
                return-object v0
            """
        )
    }
}