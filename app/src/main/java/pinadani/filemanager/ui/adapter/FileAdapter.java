package pinadani.filemanager.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pinadani.filemanager.App;
import pinadani.filemanager.Constants;
import pinadani.filemanager.R;
import pinadani.filemanager.model.FileOrFolder;
import pinadani.filemanager.utils.FileUtils;

/**
 * TODO
 */
public class FileAdapter extends BaseRecyclerViewAdapter<FileOrFolder, FileAdapter.ViewHolder> {
    public static final String TAG = FileAdapter.class.getName();

    private final DateFormat mDateFormat = new SimpleDateFormat(Constants.TRIP_DATE_FORMAT);
    private final IFileListener mFileListener;
    private boolean mHasParent = false;

    public FileAdapter(List<FileOrFolder> data, IFileListener fileListener) {
        super(data);
        mFileListener = fileListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileOrFolder fileOrFolder = getItem(position);
        holder.mRow.setOnClickListener(v -> mFileListener.onFileClicked(fileOrFolder, position));

        holder.mRow.setOnLongClickListener(v -> {
            mFileListener.onLongFileClicked(fileOrFolder, position);
            return true;
        });

        if (mHasParent && position == 0) {
            holder.mFileImg.setVisibility(View.VISIBLE);
            holder.mFileType.setVisibility(View.GONE);
            holder.mFileImg.setImageDrawable(ContextCompat.getDrawable(App.getInstance(), R.drawable.ic_arrow_up));
            holder.mFileName.setText("..");
            holder.mFileDate.setText("");
            return;
        }

        if (fileOrFolder.isDirectory()) {
            holder.mFileDate.setText(mDateFormat.format(fileOrFolder.lastModified()));
            holder.mFileImg.setImageDrawable(ContextCompat.getDrawable(App.getInstance(), R.drawable.ic_folder));
        } else {
            holder.mFileDate.setText(FileUtils.formatFileSize(fileOrFolder));
            holder.mFileType.setText(FileUtils.getFileExtension(fileOrFolder.getName()));
        }

        holder.mFileImg.setVisibility(fileOrFolder.isDirectory() ? View.VISIBLE : View.GONE);
        holder.mFileType.setVisibility(fileOrFolder.isDirectory() ? View.GONE : View.VISIBLE);

        holder.mFileSelectedLayout.setVisibility(fileOrFolder.isSelected() ? View.VISIBLE : View.GONE);

        holder.mFileName.setText(fileOrFolder.getName());


    }

    public void setParent(boolean hasParent) {
        mHasParent = hasParent;
    }

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @Bind(R.id.row_file)
        RelativeLayout mRow;
        @Bind(R.id.text_folder_name)
        TextView mFileName;
        @Bind(R.id.text_folder_date)
        TextView mFileDate;
        @Bind(R.id.img_file)
        ImageView mFileImg;
        @Bind(R.id.txt_file_type)
        TextView mFileType;
        @Bind(R.id.layout_img_file)
        RelativeLayout mFileImgLayout;
        @Bind(R.id.img_selected_file)
        RelativeLayout mFileSelectedLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * TODO
     */
    public interface IFileListener {
        void onFileClicked(FileOrFolder file, int position);

        void onLongFileClicked(FileOrFolder file, int position);
    }
}