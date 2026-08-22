package app.template.patches.gtsisic

import app.morphe.patcher.core.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstructionInvokeVirtual
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference

@Suppress("unused")
val enableCardScreenshotPatch = bytecodePatch(
    name = "Enable card screenshot",
    description = "Replaces window.addFlags(FLAG_SECURE) with clearFlags to allow screenshots."
) {
    compatibleWith(
        Compatibility(
            name = "GTSISIC",
            packageName = "com.bootiq2.gtsisic"
        )
    )

    execute {
        // Najdeme metodu podle fingerprintu
        val method = SecureFlagMethodFingerprint.method ?: return@execute
        val instructions = method.implementation?.instructions ?: return@execute

        // Projdeme instrukce a nahradíme invoke-virtual addFlags za clearFlags
        for (i in instructions.indices) {
            val insn = instructions[i]
            if (insn.opcode == Opcode.INVOKE_VIRTUAL) {
                val methodRef = (insn as? ReferenceInstruction)?.reference as? MethodReference
                if (methodRef?.name == "addFlags" &&
                    methodRef.parameterTypes?.firstOrNull() == "I"
                ) {
                    // Vytvoříme novou referenci na clearFlags se stejnými parametry
                    val newMethodRef = ImmutableMethodReference(
                        definingClass = methodRef.definingClass,
                        name = "clearFlags",
                        parameterTypes = methodRef.parameterTypes?.toList() ?: emptyList(),
                        returnType = methodRef.returnType
                    )
                    // Vytvoříme novou instrukci a nahradíme původní
                    val newInsn = ImmutableInstructionInvokeVirtual(newMethodRef)
                    method.replaceInstruction(i, newInsn)
                }
            }
        }
    }
}