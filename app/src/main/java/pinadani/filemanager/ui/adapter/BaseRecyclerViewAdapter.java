package pinadani.filemanager.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Base class for recycler view adapter
 **/
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mData;
    private OnItemClickListener<T> mListener;

    public BaseRecyclerViewAdapter(List<T> data) {
        mData = data;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    protected List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    protected T getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View rootView;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getItem(getAdapterPosition()));
            }
        }
    }
}