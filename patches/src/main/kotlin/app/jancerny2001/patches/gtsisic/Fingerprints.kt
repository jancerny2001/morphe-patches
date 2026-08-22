package app.jancerny2001.patches.gtsisic

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

object SecureFlagMethodFingerprint : Fingerprint(
    definingClass = "L",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    accessFlags = listOf(AccessFlags.PUBLIC),
    filters = listOf(
        literal(8192),
        methodCall("Landroid/view/Window;->addFlags(I)V"),
        methodCall("Landroid/view/Window;->clearFlags(I)V")
    )
)