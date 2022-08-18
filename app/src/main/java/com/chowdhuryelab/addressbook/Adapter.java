package com.chowdhuryelab.addressbook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public Adapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView phn1;
        CircleImageView profileImageView;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView_rName);
            phn1 = itemView.findViewById(R.id.textView_rPhn);
            profileImageView = itemView.findViewById(R.id.rprofileImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view == itemView)
            {
                Intent i = new Intent(mContext, read_data.class);
                i.putExtra("GetID", String.valueOf(view.getTag()));
                mContext.startActivity(i);

                System.out.println("Item clicked: "+view.getTag());
            }

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.conatct_lstview, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

            String id    =mCursor.getString( 0);
            String name  =mCursor.getString(1);
            String phn1  =mCursor.getString(2);
            String phn2   =mCursor.getString(3);
            String email =mCursor.getString(4);
            String address =mCursor.getString(5);
            byte[] img = mCursor.getBlob(6);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);



            holder.name.setText(String.format(name));
            holder.phn1.setText(String.format(phn1));
            holder.profileImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
            holder.itemView.setTag(id);


    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}