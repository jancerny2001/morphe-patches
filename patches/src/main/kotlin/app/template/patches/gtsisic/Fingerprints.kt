package app.template.patches.gtsisic

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fingerprint.AccessFlags
import app.morphe.patcher.fingerprint.InstructionLocation
import app.morphe.patcher.fingerprint.methodCall
import app.morphe.patcher.fingerprint.opcode
import com.android.tools.smali.dexlib2.Opcode

/**
 * Fingerprint pro metodu, která obsahuje logiku FLAG_SECURE.
 * Hledáme metodu:
 * - public
 * - vrací Object
 * - přijímá jediný parametr typu Object
 * - obsahuje konstantu 8192 (FLAG_SECURE)
 * - obsahuje volání window.addFlags(int) a window.clearFlags(int)
 *
 * Tento fingerprint je nezávislý na obfuskovaných názvech tříd a metod.
 */
object SecureFlagMethodFingerprint : Fingerprint(
    definingClass = "L",                               // obfuskovaná třída – jen typ
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    accessFlags = listOf(AccessFlags.PUBLIC),
    filters = listOf(
        // 1. Někde v metodě je konstanta 8192 (FLAG_SECURE)
        opcode(Opcode.CONST_16, 8192, InstructionLocation.Anywhere),
        
        // 2. Někde je volání addFlags na Window s jedním int parametrem
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "addFlags",
            parameterTypes = listOf("I"),
            location = InstructionLocation.After(1)
        ),
        
        // 3. A také volání clearFlags se stejným parametrem
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "clearFlags",
            parameterTypes = listOf("I"),
            location = InstructionLocation.After(2)
        )
    )
)