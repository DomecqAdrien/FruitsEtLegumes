package kagura.project.com.fruitlegumes.collections

import android.view.View

class Card {
    lateinit var viewFront: View
    lateinit var viewBack: View
    lateinit var view: View
    var position: Int

    constructor(viewFront: View, viewBack: View, position: Int) {
        this.viewFront = viewFront
        this.viewBack = viewBack
        this.position = position
    }

    constructor(view: View, position: Int) {
        this.view = view
        this.position = position
    }
}