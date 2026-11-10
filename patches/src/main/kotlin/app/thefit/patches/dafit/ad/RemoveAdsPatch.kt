package app.thefit.patches.dafit.ad

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import app.thefit.patches.shared.Constants.COMPATIBILITY_DAFIT

@Suppress("unused")
val removeAdsPatch = patch(
    name = "Remove ads",
    description = "Removes in-app promotional banner ads from Da Fit.",
    default = true
) {
    compatibleWith(COMPATIBILITY_DAFIT)

    execute {
        // 1. Prevent BandExtendProvider from returning any ExtendMenuModel ad/promotional items.
        BandExtendListFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return-object v0
            """
        )

        // 2. Short-circuit the BandSettingFragment presenter ad-check method (La8/a;->d()V)
        // so it never fetches ad banner images or triggers the banner view callback.
        BandSettingPresenterAdFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )

        // 3. Force BandSettingFragment's banner display callback to keep ivAdPreview hidden (View.GONE = 8)
        // and return immediately without loading Picasso images or attaching click listeners.
        BandSettingShowAdFingerprint.method.addInstructions(
            0,
            """
                iget-object v0, p0, Lcom/crrepa/band/my/home/device/BandSettingFragment;->ivAdPreview:Landroid/widget/ImageView;
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/widget/ImageView;->setVisibility(I)V
                return-void
            """
        )
    }
}
