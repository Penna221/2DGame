package ui;

public abstract class FunctionalElement extends UIElement{
    public FunctionalElement(int x, int y, int width, int height) {
        super(x, y);
    }
    public void update(){
        
        updateAdditional();
    }
    public abstract void updateAdditional();
    public abstract void task();
}
