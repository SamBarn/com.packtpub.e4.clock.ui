package com.packtpub.e4.clock.ui.views;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.*;

public class ClockWidget extends Canvas{
	private final Color color;
	public ClockWidget(Composite parent, int style, RGB rgb) {
		super(parent, style);
		//FIXME color is leaked!
		this.color = new Color(parent.getDisplay(),rgb);
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				ClockWidget.this.paintControl(e);
			}
		});
		
		new Thread("TickTock") {
			public void run() {
				while (!ClockWidget.this.isDisposed()) {
					ClockWidget.this.getDisplay().asyncExec(
							new Runnable() {
								public void run() {
									if (!ClockWidget.this.isDisposed())
										ClockWidget.this.redraw();
								}
							});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
	}
	
	@SuppressWarnings("deprecation")
	protected void paintControl(PaintEvent e) {
		e.gc.drawArc(e.x,e.y,e.width-1,e.height-1,0,360);
		int seconds = new Date().getSeconds();
		int arc = (15-seconds) * 6 % 360;
		//org.eclipse.swt.graphics.Color blue = e.display.getSystemColor(SWT.COLOR_BLUE);
		e.gc.setBackground(color);
		e.gc.fillArc(e.x,e.y,e.width-1,e.height-1,arc-1,2);
	}
	
	public Point computeSize(int w,int h,boolean changed) {
		int size;
		if(w == SWT.DEFAULT) {
			size = h;
		} else if (h == SWT.DEFAULT) {
			size = w;
		} else {
			size = Math.min(w,h);
		}
		if(size == SWT.DEFAULT)
			size = 50;
		return new Point(size,size);
	}
	
}
