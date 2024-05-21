package com.kms.smartsiren.Activity;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kms.smartsiren.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER1 = 0;
    public static final int HEADER2 = 1;

    public static final int HEADER3 = 2;
    public static final int CHILD1 = 3;
    public static final int CHILD2 = 4;
    public static final int CHILD3 = 5;

    private List<Item> data;

    private OnChildItemClickListener childItemClickListener;
    private OnChild3DataListener child3DataListener;

    // 인터페이스 정의
    public interface OnChildItemClickListener {
        void onChildItemClick();
        void onChild2ItemClick(int buttonId); // CHILD2 아이템 클릭을 위한 새로운 메소드
    }
    public interface OnChild3DataListener {
        void onChild3DataChanged(String title, String content);
    }

    public ExpandableListAdapter(List<Item> data, OnChildItemClickListener listener, OnChild3DataListener child3DataListener) {
        this.data = data;
        this.childItemClickListener = listener;
        this.child3DataListener = child3DataListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        switch (type) {
            case HEADER1:
                LayoutInflater inflaterHeader1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterHeader1.inflate(R.layout.header1, parent, false);
                ListHeaderViewHolder header1 = new ListHeaderViewHolder(view);
                return header1;
            case HEADER2:
                LayoutInflater inflaterHeader2 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterHeader2.inflate(R.layout.header2, parent, false);
                ListHeaderViewHolder header2 = new ListHeaderViewHolder(view);
                return header2;
            case HEADER3:
                LayoutInflater inflaterHeader = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterHeader.inflate(R.layout.header3, parent, false);
                ListHeaderViewHolder header3 = new ListHeaderViewHolder(view);
                return header3;
            case CHILD1:
                LayoutInflater inflaterChild1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterChild1.inflate(R.layout.child1, parent, false);
                ListChildViewHolder child1 = new ListChildViewHolder(view);
                return child1;
            case CHILD2:
                LayoutInflater inflaterChild2 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterChild2.inflate(R.layout.child2, parent, false);
                ListChildViewHolder child2 = new ListChildViewHolder(view);
                return child2;
            case CHILD3:
                LayoutInflater inflaterChild3 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterChild3.inflate(R.layout.child3, parent, false);
                ListChildViewHolder child3 = new ListChildViewHolder(view);
                return child3;

        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);

        switch (item.type) {
            case HEADER1:
            case HEADER2:
            case HEADER3:
                final ListHeaderViewHolder headerHolder = (ListHeaderViewHolder) holder;
                headerHolder.refferalItem = item;
                headerHolder.header_title.setText(item.text);

                if (item.invisibleChildren == null) {
                    headerHolder.btn_expand_toggle.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    headerHolder.btn_expand_toggle.setImageResource(R.drawable.ic_arrow_down);
                }

                headerHolder.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = data.indexOf(item);
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<>();
                            int count = 0;
                            // Find the correct header item by looping and checking the type
                            while (data.size() > pos + 1 && data.get(pos + 1).type != HEADER1 &&
                                    data.get(pos + 1).type != HEADER2 && data.get(pos + 1).type != HEADER3) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            headerHolder.btn_expand_toggle.setImageResource(R.drawable.ic_arrow_down);
                        } else {
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            headerHolder.btn_expand_toggle.setImageResource(R.drawable.ic_arrow_up);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD1:
                //B
                final ListChildViewHolder childHolder1 = (ListChildViewHolder) holder;
                childHolder1.refferalItem = item;
                childHolder1.child_title.setText(item.text);
                // 버튼 클릭 리스너 설정
                if (childHolder1.btn_reportMap != null) {
                    childHolder1.btn_reportMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (childItemClickListener != null) {
                                childItemClickListener.onChildItemClick();
                            }
                        }
                    });
                }
                break;
            case CHILD2:
                final ListChildViewHolder childHolder2 = (ListChildViewHolder) holder;
                childHolder2.refferalItem = item;

                View.OnClickListener buttonClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 현재 버튼의 선택 상태를 토글
                        boolean isSelected = v.isSelected();
                        v.setSelected(!isSelected);

                        // 모든 버튼의 상태를 초기화 (현재 버튼 제외)
                        for (Button button : childHolder2.buttons) {
                            if (button != v) { // 현재 버튼은 상태 변경을 유지
                                button.setSelected(false);
                            }
                        }

                        // 클릭 이벤트 처리
                        if (childItemClickListener != null) {
                            int buttonId = Arrays.asList(childHolder2.buttons).indexOf(v) + 1;
                            childItemClickListener.onChild2ItemClick(isSelected ? 0 : buttonId); // 선택 해제 시 0을 전달
                        }
                    }
                };

                for (Button button : childHolder2.buttons) {
                    button.setOnClickListener(buttonClickListener);
                }
                break;
            case CHILD3:
                final ListChildViewHolder childHolder3 = (ListChildViewHolder) holder;
                childHolder3.refferalItem = item;
                childHolder3.child_title.setText(item.text);
                // Perform additional tasks for CHILD1, CHILD2, CHILD3 if needed

                // EditText의 텍스트 변경 리스너 설정
                childHolder3.reportTitle.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // 이 메소드는 텍스트가 변경되기 전에 호출됩니다.
                        // 여기에 코드를 작성하거나 비워 둘 수 있습니다.
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // 이 메소드는 텍스트가 변경되는 동안 호출됩니다.
                        // 여기에 코드를 작성하거나 비워 둘 수 있습니다.
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (child3DataListener != null) {
                            child3DataListener.onChild3DataChanged(s.toString(), childHolder3.reportContent.getText().toString());
                        }
                    }
                    // beforeTextChanged와 onTextChanged 구현 생략
                });

                childHolder3.reportContent.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // 이 메소드는 텍스트가 변경되기 전에 호출됩니다.
                        // 여기에 코드를 작성하거나 비워 둘 수 있습니다.
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // 이 메소드는 텍스트가 변경되는 동안 호출됩니다.
                        // 여기에 코드를 작성하거나 비워 둘 수 있습니다.
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (child3DataListener != null) {
                            child3DataListener.onChild3DataChanged(childHolder3.reportTitle.getText().toString(), s.toString());
                        }
                    }
                    // beforeTextChanged와 onTextChanged 구현 생략
                });
                break;
        }
    }



    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    }
    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView child_title;
        //B
        public Button btn_reportMap;
        public Button btn_dan1;
        public Button btn_dan2;
        public Button btn_dan3;
        public Button btn_dan4;
        public Button btn_dan5;
        public Button btn_dan6;
        public Item refferalItem;
        public Button[] buttons;
        public EditText reportTitle;
        public EditText reportContent;
        public ListChildViewHolder(View itemView) {
            super(itemView);
            child_title = (TextView) itemView.findViewById(R.id.child_title);
            //B
            btn_reportMap = (Button) itemView.findViewById((R.id.btn_reportMap));
            btn_dan1 = itemView.findViewById(R.id.btn_dan1);
            btn_dan2 = itemView.findViewById(R.id.btn_dan2);
            btn_dan3 = itemView.findViewById(R.id.btn_dan3);
            btn_dan4 = itemView.findViewById(R.id.btn_dan4);
            btn_dan5 = itemView.findViewById(R.id.btn_dan5);
            btn_dan6 = itemView.findViewById(R.id.btn_dan6);
            buttons = new Button[] {
                    btn_dan1, btn_dan2, btn_dan3, btn_dan4, btn_dan5, btn_dan6
            };

            reportTitle = itemView.findViewById(R.id.report_title);
            reportContent = itemView.findViewById(R.id.report_content);
        }
    }

    public static class Item {
        public int type;
        public String text;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }
}
