package app.template.patches.gtsisic

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fingerprint.AccessFlags
import app.morphe.patcher.fingerprint.InstructionLocation
import app.morphe.patcher.fingerprint.methodCall
import app.morphe.patcher.fingerprint.opcode
import com.android.tools.smali.dexlib2.Opcode

/**
 * Fingerprint pro metodu, která nastavuje FLAG_SECURE.
 * Hledá metodu:
 * - public
 * - vrací Object
 * - přijímá jeden parametr typu Object
 * - obsahuje konstantu 8192 (FLAG_SECURE)
 * - obsahuje volání window.addFlags(int)
 * - obsahuje volání window.clearFlags(int)
 *
 * Tento fingerprint je nezávislý na obfuskovaných názvech tříd a metod.
 */
object SecureFlagMethodFingerprint : Fingerprint(
    // Třída je obfuskovaná, stačí uvést jen objektový typ
    definingClass = "L",
    // Přesný návratový typ
    returnType = "Ljava/lang/Object;",
    // Jediný parametr typu Object
    parameters = listOf("Ljava/lang/Object;"),
    // Přístupová práva – public
    accessFlags = listOf(AccessFlags.PUBLIC),
    // Filtry instrukcí hledané v metodě
    filters = listOf(
        // 1. Někde v metodě je konstanta 8192 (FLAG_SECURE)
        opcode(Opcode.CONST_16, 8192, InstructionLocation.Anywhere),
        // 2. Volání window.addFlags(int)
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "addFlags",
            parameterTypes = listOf("I")
        ),
        // 3. Volání window.clearFlags(int)
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "clearFlags",
            parameterTypes = listOf("I")
        )
    )
)