/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.loopeer.test.itemtouchhelperextension;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_DIVIDER_HEIGHT = 1;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    protected int mOrientation;
    protected int padding;
    protected int startpadding;
    protected int endpadding;
    protected int dividerHeight;
    protected Context mContext;
    protected Paint mPaddingPaint;
    protected Paint mDividerPaint;

    public DividerItemDecoration(Context context) {
        this(context, VERTICAL_LIST, -1, -1);
    }

    public DividerItemDecoration(Context context, int orientation) {
        this(context, orientation, -1, -1);
    }

    public DividerItemDecoration(Context context, int orientation, int padding, int dividerHeight) {
        setOrientation(orientation);
        mContext = context;

        init();
        if (padding != -1) this.padding = padding;
        updatePaddint();
        if (dividerHeight != -1) this.dividerHeight = dividerHeight;
    }

    public DividerItemDecoration(Context context, int orientation, int startpadding, int endpadding, int dividerHeight) {
        setOrientation(orientation);
        mContext = context;

        init();
        if (startpadding != -1) this.startpadding = startpadding;
        if (endpadding != -1) this.endpadding = endpadding;
        if (dividerHeight != -1) this.dividerHeight = dividerHeight;
    }

    private void updatePaddint() {
        startpadding = padding;
        endpadding = padding;
    }

    private void init() {
        padding = mContext.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        updatePaddint();
        dividerHeight = DEFAULT_DIVIDER_HEIGHT;

        mPaddingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaddingPaint.setColor(ContextCompat.getColor(mContext, android.R.color.white));
        mPaddingPaint.setStyle(Paint.Style.FILL);

        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
        mDividerPaint.setStyle(Paint.Style.FILL);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + dividerHeight;

            c.drawRect(left, top, left + startpadding, bottom, mPaddingPaint);
            c.drawRect(right - endpadding, top, right, bottom, mPaddingPaint);
            c.drawRect(left + startpadding, top, right - endpadding, bottom, mDividerPaint);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + dividerHeight;
            c.drawRect(left, top, right, top + startpadding, mPaddingPaint);
            c.drawRect(left, bottom - endpadding, right, bottom, mPaddingPaint);
            c.drawRect(left, top + startpadding, right, bottom - endpadding, mDividerPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.set(0, 0, 0, dividerHeight);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.set(0, 0, dividerHeight, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }

    }
}
