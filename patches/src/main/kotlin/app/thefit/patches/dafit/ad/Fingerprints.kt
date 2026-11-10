package app.thefit.patches.dafit.ad

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

/**
 * Matches `BandExtendProvider.getExtendList()Ljava/util/List;` which loads `ExtendMenuModel`
 * ad and promotional entries from `BandConfig.getExtendMenu()`.
 */
internal object BandExtendListFingerprint : Fingerprint(
    definingClass = "Lcom/crrepa/band/my/model/band/provider/BandExtendProvider;",
    name = "getExtendList",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Ljava/util/List;",
    parameters = emptyList()
)

/**
 * Matches the `BandSettingFragment` presenter method (`La8/a;->d()V`) that iterates over
 * `BandExtendProvider.getExtendList()`, checks for `ExtendMenuModel.AD` (`type == 4`),
 * downloads the ad banner via Picasso, and invokes the view callback.
 */
internal object BandSettingPresenterAdFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = emptyList(),
    filters = listOf(
        methodCall(
            opcode = Opcode.INVOKE_STATIC,
            definingClass = "Lcom/crrepa/band/my/model/band/provider/BandExtendProvider;",
            name = "getExtendList"
        ),
        methodCall(
            opcode = Opcode.INVOKE_VIRTUAL,
            definingClass = "Lcom/crrepa/band/my/model/ExtendMenuModel;",
            name = "getType"
        )
    )
)

/**
 * Matches `BandSettingFragment->X(Lcom/squareup/picasso/t;Ljava/lang/String;)V` which sets
 * `ivAdPreview.setVisibility(View.VISIBLE)` and attaches the ad URL click listener.
 */
internal object BandSettingShowAdFingerprint : Fingerprint(
    definingClass = "Lcom/crrepa/band/my/home/device/BandSettingFragment;",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("L", "Ljava/lang/String;"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            smali = "Lcom/crrepa/band/my/home/device/BandSettingFragment;->ivAdPreview:Landroid/widget/ImageView;"
        ),
        methodCall(
            opcode = Opcode.INVOKE_VIRTUAL,
            definingClass = "Landroid/widget/ImageView;",
            name = "setVisibility"
        )
    )
)
