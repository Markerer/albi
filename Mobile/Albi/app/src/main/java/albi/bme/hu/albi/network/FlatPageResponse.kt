package albi.bme.hu.albi.network

import albi.bme.hu.albi.model.Flat

class FlatPageResponse {
    //@SerializedName("docs")
    var docs: List<Flat>? = null

    var total: Int? = null
    var limit: Int? = null
    var page: String? = null //nemár
    var pages: Int? = null
}