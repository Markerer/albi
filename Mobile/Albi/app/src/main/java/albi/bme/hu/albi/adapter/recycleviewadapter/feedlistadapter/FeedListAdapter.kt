//package albi.bme.hu.albi.adapter.recycleviewadapter.feedlistadapter
//
//import albi.bme.hu.albi.databinding.NetworkItemBinding
//import albi.bme.hu.albi.model.Flat
//import albi.bme.hu.albi.network.NetworkState
//
//import android.arch.paging.PagedListAdapter
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//
//
//class FeedListAdapter(private val context: Context) : PagedListAdapter<Flat, RecyclerView.ViewHolder>(Flat.DIFF_CALLBACK) {
//    private var networkState: NetworkState? = null
//
//    //NetworkItemBinding meg a másik valahol a generated java package-ben van
//    //Valamit kéne azokkal kezdeni
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        if (viewType == TYPE_PROGRESS) {
//            val headerBinding = NetworkItemBinding.inflate(layoutInflater, parent, false)
//            return NetworkStateItemViewHolder(headerBinding)
//
//        } else {
//            val itemBinding = FeedItemBinding.inflate(layoutInflater, parent, false)
//            return FlatItemViewHolder(itemBinding)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is FlatItemViewHolder) {
//            holder.bindTo(getItem(position))
//        } else {
//            (holder as NetworkStateItemViewHolder).bindView(networkState)
//        }
//    }
//
//
//    private fun hasExtraRow(): Boolean {
//        return networkState != null && networkState !== NetworkState.LOADED
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (hasExtraRow() && position == itemCount - 1) {
//            TYPE_PROGRESS
//        } else {
//            TYPE_ITEM
//        }
//    }
//
//    fun setNetworkState(newNetworkState: NetworkState) {
//        val previousState = this.networkState
//        val previousExtraRow = hasExtraRow()
//        this.networkState = newNetworkState
//        val newExtraRow = hasExtraRow()
//        if (previousExtraRow != newExtraRow) {
//            if (previousExtraRow) {
//                notifyItemRemoved(itemCount)
//            } else {
//                notifyItemInserted(itemCount)
//            }
//        } else if (newExtraRow && previousState !== newNetworkState) {
//            notifyItemChanged(itemCount - 1)
//        }
//    }
//
//
//    inner class FlatItemViewHolder(private val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
//
//        fun bindTo(flat: Flat?) {
//            binding.itemImage.setVisibility(View.VISIBLE)
//            binding.itemDesc.setVisibility(View.VISIBLE)
//
//
//        }
//    }
//
//
//    inner class NetworkStateItemViewHolder(private val binding: NetworkItemBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
//
//        fun bindView(networkState: NetworkState?) {
//            if (networkState != null && networkState.status === NetworkState.Status.RUNNING) {
//                binding.progressBar.setVisibility(View.VISIBLE)
//            } else {
//                binding.progressBar.setVisibility(View.GONE)
//            }
//
//            if (networkState != null && networkState.status === NetworkState.Status.FAILED) {
//                binding.errorMsg.setVisibility(View.VISIBLE)
//                binding.errorMsg.setText(networkState.msg)
//            } else {
//                binding.errorMsg.setVisibility(View.GONE)
//            }
//        }
//    }
//
//    companion object {
//
//        private val TYPE_PROGRESS = 0
//        private val TYPE_ITEM = 1
//    }
//}
