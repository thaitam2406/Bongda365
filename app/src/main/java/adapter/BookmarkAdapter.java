package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import library.database.DatabaseManagement;
import library.serviceAPI.loader.ImageLoader;
import model.Bookmark;

public class BookmarkAdapter extends BaseAdapter {
	private Context mContext;
	private int mRes;
	private ArrayList<Bookmark> mNews;
	private ImageLoader imageLoader;

	public BookmarkAdapter(Context context,
			ArrayList<Bookmark> objects) {
		this.mContext = context;
		this.mNews = objects;
		imageLoader = new ImageLoader(context);
	}
	
	private class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView icon_delete;
		public ImageView thumbImg;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNews.size();
	}


	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mNews.get(arg0).getIDNews();
	}


	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if(convertView == null)
		{
			convertView = View.inflate(mContext, R.layout.item_bookmark, null);
			initUI(holder,convertView);
			convertView.setTag(holder);

		}
		else
			holder = (ViewHolder) convertView.getTag();

        Bookmark bookmark = mNews.get(position);
        initData(bookmark,holder,position);


		return convertView;
	}

    private void initData(final Bookmark bookmark, ViewHolder holder, final int position) {
        holder.tvTitle.setText(bookmark.getTitle());
        imageLoader.DisplayImage(bookmark.getThumbImg(),
                holder.thumbImg);
        holder.tvDate.setText(bookmark.getCreateDate());
        holder.icon_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatabaseManagement(mContext).deleteBookmark(bookmark.getIDNews());
                mNews.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void initUI(ViewHolder holder, View convertView) {
        holder.tvTitle = (TextView) convertView.findViewById(R.id.titleBookmark);
        holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        holder.thumbImg = (ImageView) convertView.findViewById(R.id.imgIcon);
        holder.icon_delete = (ImageView) convertView.findViewById(R.id.img_delete);
    }
}
