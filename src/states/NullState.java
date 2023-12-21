package states;

import java.awt.Graphics;

public class NullState extends State{

    @Override
    public void update() {if(!running){State.transition.update();}}

    @Override
    public void render(Graphics g) {if(transition !=null){transition.render(g);}}

    @Override
    public void init() {}

    @Override
    public void updateOnceBetweenTransitions() {}
    
}
