package com.putu.gitnet.model

import com.google.gson.annotations.SerializedName

data class ResponseModel (

        @field:SerializedName("total_count")
        val totalCount: Int,

        @field:SerializedName("incomplete_results")
        val incompleteResults: Boolean,

        @field:SerializedName("items")
        val items: List<UserModel>

        )