package com.betterdevs.betterme.ui.navigation

interface Destination {
    val path: String
    val showBottomBar: Boolean
}

object Destinations {
    fun getAllDestinations(): List<Destination> = listOf(
        Login,
        CreateAccount,
        Posts,
        PostCreation,
        StatisticSelection,
        Statistics,
        Profile
    )

    object Login : Destination {
        override val path = "Login"
        override val showBottomBar = false
    }

    object CreateAccount : Destination {
        override val path = "CreateAccount"
        override val showBottomBar = false
    }

    object Posts : Destination {
        override val path = "Posts"
        override val showBottomBar = true
    }

    object PostCreation : Destination {
        override val path = "Posts/PostCreation"
        override val showBottomBar = true
    }

    object StatisticSelection : Destination {
        override val path = "StatisticSelection"
        override val showBottomBar = true
    }

    object Statistics : Destination {
        override val path = "Statistics"
        override val showBottomBar = true

        const val ARG_CATEGORY = "category"
        val routeWithArgs = "$path/{$ARG_CATEGORY}"
    }

    object Profile : Destination {
        override val path = "Profile"
        override val showBottomBar = true
    }
}
