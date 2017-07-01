package pinadani.filemanager.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pinadani.filemanager.App;
import pinadani.filemanager.Constants;
import pinadani.filemanager.R;
import pinadani.filemanager.utils.FileUtils;

/**
 * TODO
 */
public class FileAdapter extends BaseRecyclerViewAdapter<File, FileAdapter.ViewHolder> {
    public static final String TAG = FileAdapter.class.getName();

    private final DateFormat mDateFormat = new SimpleDateFormat(Constants.TRIP_DATE_FORMAT);

    public FileAdapter(List<File> data) {
        super(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = getItem(position);
        if (file.isDirectory()) {
            holder.mFileDate.setText(mDateFormat.format(file.lastModified()));
        } else {
            holder.mFileDate.setText(FileUtils.formatFileSize(file));
            holder.mFileImg.setImageDrawable(ContextCompat.getDrawable(App.getInstance(), R.drawable.ic_file));
        }
        holder.mFileName.setText(file.getName());
    }


    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @Bind(R.id.text_folder_name)
        TextView mFileName;
        @Bind(R.id.text_folder_date)
        TextView mFileDate;
        @Bind(R.id.img_file)
        ImageView mFileImg;
        @Bind(R.id.layout_img_file)
        RelativeLayout mFileImgLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface IFileListener {
        void onFileClicked(File file);

        void onLongFileClicked(File file);
    }
}