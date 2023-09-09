package com.bestandroidaboudemaybas.masebha;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<CardData> cardDataList;
    private Context context;
    private OnItemDeletedListener itemDeletedListener;
    private Integer cardBackgroundColor;
    private Integer cardTextColor;

    public MyAdapter(List<CardData> cardDataList, Context context, OnItemDeletedListener itemDeletedListener, Integer cardBackgroundColor, Integer cardTextColor) {
        this.cardDataList = cardDataList;
        this.context = context;
        this.itemDeletedListener = itemDeletedListener;
        this.cardBackgroundColor = cardBackgroundColor;
        this.cardTextColor = cardTextColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview, parent, false);
        return new ViewHolder(view,this, context,itemDeletedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardData cardData = cardDataList.get(position);
        holder.titleTextView.setText(cardData.getTitle());
        holder.titleTextView.setTextColor(cardTextColor);
        holder.descriptionTextView.setText(cardData.getDescription());
        holder.descriptionTextView.setTextColor(cardTextColor);
        holder.moreOption.setColorFilter(cardTextColor);
        CardView cardView = (CardView) holder.itemView;
        cardView.setCardBackgroundColor(cardBackgroundColor);
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    public interface OnItemDeletedListener {
        void onItemDeleted(int total);
    }
    public void changeCardColors(int color,int textColor){
        cardBackgroundColor = color;
        cardTextColor = textColor;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView moreOption;
        MyAdapter adapter;
        SQLiteDatabase myDB;
        public ViewHolder(View itemView, MyAdapter adapter, Context context,OnItemDeletedListener itemDeletedListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            moreOption = itemView.findViewById(R.id.more_option);
            this.adapter = adapter;





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();


                    if (position != RecyclerView.NO_POSITION) {

                        CardData cardData = adapter.cardDataList.get(position);

                        long cardId = cardData.getId();

                        Intent intent = new Intent(itemView.getContext(), MainActivity2.class);

                        intent.putExtra("clickedCardId", cardData.getId());

                        itemView.getContext().startActivity(intent);
                    }
                }
            });



            moreOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.more_menu, popupMenu.getMenu());

                    //change color of حذف
                    MenuItem deleteMenuItem = popupMenu.getMenu().findItem(R.id.menu_delete);
                    SpannableString s = new SpannableString("حذف");
                    s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
                    deleteMenuItem.setTitle(s);


                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            if(item.getItemId()==R.id.menu_delete) {

                                showDeleteConfirmationDialogAndDelete();


                                return true;

                            }




                            else if(item.getItemId() == R.id.menu_edit) {

                                int position = getAdapterPosition();
                                CardData cardData = adapter.cardDataList.get(position);

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = LayoutInflater.from(context);
                                View dialogView = inflater.inflate(R.layout.add_dialog, null);
                                dialogBuilder.setView(dialogView);
                                AlertDialog alertDialog = dialogBuilder.create();

                                myDB = DatabaseManager.getDatabase(context);




                                EditText editText1 = dialogView.findViewById(R.id.name);
                                EditText editText2 = dialogView.findViewById(R.id.number);
                                Button addButton = dialogView.findViewById(R.id.addButton);
                                Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                                addButton.setText("تعديل");
                                editText1.setText(cardData.getTitle());
                                editText2.setText(cardData.getDawra().toString());


                                addButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (TextUtils.isEmpty(editText1.getText().toString().trim()) || TextUtils.isEmpty(editText2.getText().toString().trim())) {
                                            Toast.makeText(context, "الرجاء ملء الخانات", Toast.LENGTH_LONG).show();
                                        }
                                        else if (Integer.parseInt(editText2.getText().toString())==0) {
                                            Toast.makeText(context, "عدد الحبات لا يجب ان يساوي صفر", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            String name = editText1.getText().toString();
                                            String number = editText2.getText().toString();


                                            ContentValues editedZeker = new ContentValues();
                                            editedZeker.put("name", name);
                                            editedZeker.put("dawra", Integer.parseInt(number));
                                            myDB.update("zeker", editedZeker, "_id=?", new String[] { String.valueOf(cardData.getId()) });
                                            DatabaseManager.closeDatabase();


                                            adapter.cardDataList.get(position).setDawra(Integer.parseInt(number));
                                            adapter.cardDataList.get(position).setTitle(name);

                                            adapter.notifyItemChanged(position);
                                            alertDialog.dismiss();
                                            }

                                    }
                                });

                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });


                                alertDialog.show();

                                return true;
                            }

                            return false;
                        }




                        private void showDeleteConfirmationDialogAndDelete() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("هل أنت متأكد أنك تريد حذف هذا الذكر؟")
                                    .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            int position = getAdapterPosition();

                                            if (position != RecyclerView.NO_POSITION) {

                                                CardData cardData = adapter.cardDataList.get(position);

                                                myDB = DatabaseManager.getDatabase(context);

                                                long cardId = cardData.getId();


                                                //I want the total to edit "majmu3" in MainActivity, i will get it from my cursor, it's easier
                                                Cursor myCursor = myDB.rawQuery("select * from zeker where _id = "+ cardId, null);
                                                myCursor.moveToFirst();
                                                Integer cardTotal = myCursor.getInt(2);
                                                myCursor.close();
                                                if (itemDeletedListener != null) {
                                                    itemDeletedListener.onItemDeleted(cardTotal);
                                                }


                                                myDB.execSQL("DELETE FROM zeker WHERE _id = "+ cardId);

                                                DatabaseManager.closeDatabase();
                                                adapter.cardDataList.remove(position);


                                                adapter.notifyItemRemoved(position);

                                            }
                                        }
                                    })
                                    .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog, do nothing or handle accordingly
                                        }
                                    });

                            // Create and show the AlertDialog
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    popupMenu.show();
                }
                });
        }

    }
}
