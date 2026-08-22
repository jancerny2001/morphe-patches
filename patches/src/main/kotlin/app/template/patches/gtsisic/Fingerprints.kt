package app.jancerny2001.patches.gtsisic

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fingerprint.AccessFlags
import app.morphe.patcher.fingerprint.literal
import app.morphe.patcher.fingerprint.methodCall

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