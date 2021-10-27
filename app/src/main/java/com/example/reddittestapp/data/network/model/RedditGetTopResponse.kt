package com.example.reddittestapp.data.network.model

data class RedditGetTopResponse(
    var data: DataChildren?,
    var kind: String?
) {
    data class DataChildren(
        var after: String?,
        var before: String?,
        var children: List<Children>?,
        ) {
        data class Children(
            var data: DataTop?,
            var kind: String?
        ) {
            data class DataTop(
                var author: String?,
                var title: String?,
                var num_comments: Int?,
                var created: Double?,
                var thumbnail: String?,
                var thumbnail_height: Int?,
                var thumbnail_width: Int?,
                var url: String?
            )
        }
    }
}