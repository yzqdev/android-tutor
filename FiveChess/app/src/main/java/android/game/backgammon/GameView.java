package android.game.backgammon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    private int gridSize = 0; // 五子棋盘每格的大小
    private int selectX = 7; // 选中网格关于棋盘的X坐标
    private int selectY = 7; // 选中网格关于棋盘的Y坐标
    private final Rect selectRect = new Rect();// 选中网格对于屏幕的方块信息
    private BackgammonActivity backAct;

    public GameView(Context context) {

        super(context);
        System.out.println("gameview的构造函数");
        this.setBackgroundResource(R.drawable.wood1);// 设置背景图
        backAct = (BackgammonActivity) context;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("GameView", "onDraw");
        Log.i("点击", "下旗子");
        drawGridboard(canvas);// 画棋盘
        drawPieces(canvas); // 画棋子

        drawSelectRect(canvas);// 画选定区域
        super.onDraw(canvas);
    }

    /**
     * 初始化和屏幕大小改变时调用该函数
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {//
        int min = Math.min(w, h);
        gridSize = min / BackgammonActivity.LINE;//宽度除以15格
        this.getRect(selectX, selectY, selectRect);
        Log.d("GameView", "onSizeChanged:gridSize=" + gridSize + ",gridSize="
                + gridSize + ".");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 通过输入相对棋盘的X和Y设置rect的值 使之变为该X和Y代表的屏幕区域值
     */
    private void getRect(int x, int y, Rect rect) {
        rect.set((int) (x * gridSize + getTopX()),
                (int) (y * gridSize + getTopY()), (int) (x * gridSize
                        + gridSize + getTopX()),
                (int) (y * gridSize + gridSize + getTopY()));
    }

    /**
     * 按键事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selectX, selectY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selectX, selectY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selectX - 1, selectY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selectX + 1, selectY);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                backAct.setPieceIfValid(selectX, selectY, 2);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {// 触摸屏事件
        this.select((int) ((event.getX() - getTopX()) / gridSize),
                (int) ((event.getY() - getTopY()) / gridSize));
        backAct.setPieceIfValid(selectX, selectY, 2);
        return super.onTouchEvent(event);
    }

    public void select(int x, int y) {// 选择区域
        this.invalidate(selectRect);
        selectX = Math.min(Math.max(x, 0), BackgammonActivity.LINE - 1);
        selectY = Math.min(Math.max(y, 0), BackgammonActivity.LINE - 1);
        this.getRect(selectX, selectY, selectRect);
        this.invalidate(selectRect);
    }

    private void drawSelectRect(Canvas canvas) {
        Paint selected = new Paint();
        selected.setColor(this.getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selectRect, selected);
    }

    /**
     * 画棋子
     *
     * @param canvas 花呗
     */
    private void drawPieces(Canvas canvas) {
        Paint whitePiece = new Paint();
        whitePiece.setColor(Color.WHITE);
        Paint blackPiece = new Paint();
        blackPiece.setColor(Color.BLACK);
        int[][] gridBoard = backAct.getGridBoard();
        Rect pieceRect = new Rect();
        for (int i = 0; i < BackgammonActivity.LINE; i++) {
            for (int j = 0; j < BackgammonActivity.LINE; j++) {
                if (gridBoard[i][j] == 1) {// 白子
                    getRect(i, j, pieceRect);
                    canvas.drawCircle(pieceRect.centerX(), pieceRect.centerY(), pieceRect.height() / 2f, whitePiece);
                    //canvas.drawRect(pieceRect, whitePiece);
                } else if (gridBoard[i][j] == 2) {// 黑子
                    getRect(i, j, pieceRect);
                    canvas.drawCircle(pieceRect.centerX(), pieceRect.centerY(), pieceRect.height() / 2f, blackPiece);
                    //canvas.drawRect(pieceRect, blackPiece);
                }
            }
        }
    }

    /**
     * 画网格线
     *
     * @param canvas 画布
     */
    private void drawGridboard(Canvas canvas) {

        Paint hilite = new Paint();
        hilite.setColor(this.getResources().getColor(R.color.puzzle_hilite));
        Paint light = new Paint();
        light.setColor(this.getResources().getColor(R.color.puzzle_light));
        for (int i = 0; i <= BackgammonActivity.LINE; i++) {
            canvas.drawLine(getTopX(), i * gridSize + getTopY(), getTopX()
                    + gridSize * BackgammonActivity.LINE, getTopY() + i
                    * gridSize, hilite);
            canvas.drawLine(getTopX() + i * gridSize, getTopY(), i * gridSize,
                    getTopY() + gridSize * BackgammonActivity.LINE, hilite);
            canvas.drawLine(getTopX(), i * gridSize + getTopY() + 1, getTopX()
                    + gridSize * BackgammonActivity.LINE, getTopY() + i
                    * gridSize + 1, light);
            canvas.drawLine(getTopX() + i * gridSize + 1, getTopY(), i
                    * gridSize + 1, getTopY() + gridSize
                    * BackgammonActivity.LINE, light);
        }
    }

    private float getTopX() {// 返回棋盘左上角的X坐标
        return 0;
    }

    /**
     * 返回棋盘左上角的Y坐标
     *
     * @return
     */
    private int getTopY() {//
        int blankSize = Math.abs(getWidth() - getHeight());
        return blankSize / 2;
    }
}