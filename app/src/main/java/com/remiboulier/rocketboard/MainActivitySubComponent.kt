package com.remiboulier.rocketboard

import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by Remi BOULIER on 01/12/2018.
 * email: boulier.r.job@gmail.com
 */

@Subcomponent(modules = [MainActivityModule.InjectTestItem::class])
interface MainActivitySubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}