// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.common.util.Log;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;


/**
 * Test FxIconBuilder.
 */
public class TestFxIconBuilder
	extends Application
{
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
	
	public void start(Stage s) throws Exception
	{
		CPane p = new CPane();
		p.setBackground(FX.background(new Color(0.3, 0.3, 0.3, 1.0)));
		p.setPadding(20);
		p.setCenter(icon());
		p.addEventFilter(MouseEvent.MOUSE_CLICKED, (ev) -> p.setCenter(icon()));
		
		Scene sc = new Scene(p);
		
		s.setScene(sc);
		s.setTitle("FxIconBuilder");
		s.show();
	}
	
	
	protected Node icon()
	{
		return makePlane();
	}


	// big X
	protected Node makeBigX()
	{
		FxIconBuilder b = new FxIconBuilder(200);
		
		b.setStrokeWidth(10);
		b.setStrokeColor(Color.WHITE);
		b.moveTo(10, 10);
		b.lineRel(180, 180);
		b.moveTo(10, 190);
		b.lineRel(180, -180);
		
		return b.getIcon();
	}
	
	
	// arcs with various sweep angle
	protected Node makeC()
	{
		FxIconBuilder b = new FxIconBuilder(200, 100, 100);
		
		b.setFill(Color.WHITE);
		b.fill();
		b.setFill(null);
		
		b.setStrokeWidth(0.2);

		b.newPath();
		b.setStrokeColor(Color.BLACK);
		b.moveTo(0, -90);
		b.lineRel(0, 180);
		b.moveTo(-90, 0);
		b.lineRel(180, 0);
		
		int steps = 12;
		for(int i=0; i<steps; i++)
		{
			double r = 90.0 * (i + 1) / (steps);
			double a = 360.0 * (i + 1) / steps;
			double c = 0.4 + 0.6 * i / (steps - 1);
			
			D.print(i, "r", r, "a", a);
			
			b.newPath();
			b.setStrokeColor(new Color(c, c, 1.0 - c, 1));
			b.setStrokeWidth(0.7);
			b.moveTo(0, -r);
			b.arcRel(0, 0, r, FX.toRadians(a));
		}
		
		return b.getIcon();
	}
	
	
	protected Node makeWait()
	{
		FxIconBuilder b = new FxIconBuilder(200, 100, 100);
		
		b.setFill(Color.gray(1, 0.1));
		b.fill();

		b.setFill(null);
		b.setStrokeWidth(0.2);
		b.setStrokeColor(Color.BLACK);

		b.newPath();
		b.moveTo(0, -90);
		b.lineRel(0, 180);
		b.moveTo(-90, 0);
		b.lineRel(180, 0);
		
		double a = Math.PI / 4;
		double r = 80;
		
		b.setStrokeWidth(10);
		b.setStrokeLineCap(StrokeLineCap.ROUND);
		b.setStrokeColor(Color.YELLOW);
		// beware of clipping
		// https://bugs.openjdk.java.net/browse/JDK-8088365
		// b.setEffect(new Bloom(0.5));

		b.newPath();
		b.moveTo(r * Math.cos(a), -r * Math.sin(a));
		b.arcRel(0, 0, r, -(Math.PI - a - a));
		b.moveTo(r * Math.cos(a), r * Math.sin(a));
		b.arcRel(0, 0, r, (Math.PI - a - a));
		
		return b.getIcon();
	}
	
	
	protected Node makePlane()
	{
		FxIconBuilder b = new FxIconBuilder(500);
		
		b.setFill(Color.gray(1, 0.7));
		b.fill();
		
		b.setOpacity(0.5);
		b.addEffect(new GaussianBlur(4));
		b.addEffect(new ColorAdjust(0, -0.8, 0, 0));
		b.setTranslate(-150, -150);
		try
		{
			b.image(CKit.readLocalBytes(TestFxIconBuilder.class, "ground.png"));
		}
		catch(Exception e)
		{
			Log.ex(e);
		}
		
		b.setEffect(null);
		b.setOpacity(1);
		b.setFill(Color.YELLOW);
		b.setStrokeColor(FX.mix(Color.YELLOW, Color.BLACK, 0.7));
		b.setStrokeWidth(10);
		b.setScale(0.1);
		b.setTranslate(-600, -600);
		b.setEffect(new DropShadow(300, 300, 300, Color.BLACK));
		b.setRotate(0.5);
		b.svgPath("m1696.665161,1345.895996c0,0 2.263428,9.05481 2.009399,18.580444c-0.242432,9.33374 -3.01416,29.730469 -3.01416,25.085327c0,-4.644897 -9.025146,-10.449951 -9.025146,-10.449951l-1.259033,8.125l-7.021362,0.235229l-0.248169,-13.010864l-213.013794,-89.416504c0,0 -0.248169,-11.150513 -1.25293,-11.380371c-0.248291,11.14502 0.502319,18.580322 -1.004761,19.505127c-2.003662,-0.694946 -235.744873,-96.775757 -240.8573,-99.407349c-2.25769,-1.159912 -1.004761,-10.679688 -1.004761,-10.679688l-60.214233,-8.360229l-0.248169,7.429688c0,0 -126.705688,-16.490234 -130.216309,-14.400269c-3.510742,2.09021 3.268555,128.20813 -2.76001,213.680054c-6.022705,85.466675 -16.306396,131.452637 -20.821777,147.942993c-4.214355,15.390747 -0.750854,18.810059 7.275513,25.315552c8.026001,6.505371 169.348145,117.062744 182.900635,126.582886c13.303833,9.35022 13.552124,13.623291 13.552124,19.275269c0,7.42981 0.248413,56.435913 0.248413,56.435913l-224.55072,-50.630737c0,0 -1.25885,20.664551 -4.515381,23.684814c-3.268494,3.025879 -10.036011,1.860229 -10.036011,1.860229c0,0 -4.521423,42.971313 -7.02124,45.756226c-2.512085,2.785156 -6.773071,4.17981 -6.773071,4.17981c0,0 -10.012268,44.312012 -14.616211,45.471924c0,0.016602 0,0.043579 0,0.054688c-0.035645,0 -0.071289,-0.021973 -0.094543,-0.027466c-0.035645,0.005493 -0.070984,0.027466 -0.094666,0.027466c0,-0.011108 0,-0.038086 0,-0.054688c-4.604065,-1.159912 -14.62207,-45.471924 -14.62207,-45.471924c0,0 -4.267578,-1.394653 -6.773438,-4.17981c-2.505859,-2.790283 -7.021545,-45.756226 -7.021545,-45.756226c0,0 -6.773071,1.165649 -10.035645,-1.860229c-3.262512,-3.020264 -4.515442,-23.684814 -4.515442,-23.684814l-224.550537,50.630737c0,0 0.247986,-49.006104 0.247986,-56.435913c0,-5.651978 0.248352,-9.925049 13.546448,-19.275269c13.546814,-9.520142 174.874695,-120.077515 182.900879,-126.582886c8.026306,-6.505493 11.489502,-9.924805 7.275452,-25.315552c-4.515381,-16.490356 -14.799377,-62.476318 -20.821899,-147.942993c-6.022705,-85.471924 0.750549,-211.589844 -2.760071,-213.680054c-3.510681,-2.089966 -130.216248,14.400269 -130.216248,14.400269l-0.248169,-7.429688l-60.214294,8.360229c0,0 1.25293,9.519775 -1.004944,10.679688c-5.112305,2.631592 -238.853577,98.712402 -240.857056,99.407349c-1.507141,-0.930176 -0.750641,-8.360107 -1.004852,-19.505127c-1.004669,0.229858 -1.253021,11.380371 -1.253021,11.380371l-213.019516,89.416504l-0.248169,13.010864l-7.021637,-0.235229l-1.25293,-8.125c0,0 -9.031006,5.810425 -9.031006,10.449951c0,4.639771 -2.765961,-15.751587 -3.008469,-25.085327c-0.248169,-9.520386 2.009674,-18.580444 2.009674,-18.580444c0,0 -7.275681,2.089722 -6.524902,-6.735229c0.484406,-5.630005 19.320663,-31.35022 19.320663,-31.35022c0,0 1.335785,-3.857422 1.105377,-6.581787c-0.266083,-3.167847 3.906494,-5.208862 6.111176,-7.019897c1.276733,-1.044922 -0.136017,-6.789673 -0.136017,-6.789673c0,0 70.586868,-56.031128 192.889267,-143.812134c122.101288,-87.63855 266.614288,-195.093994 266.614288,-195.093994c0,0 0,-6.193481 -1.335693,-14.865417c-1.341736,-8.666382 -2.340332,-12.386841 -2.340332,-12.386841c0,0 -7.358521,-2.166748 -9.367859,-7.742065c-1.495239,-4.158081 -5.348877,-34.375854 -4.681091,-61.62793c0.667908,-27.252625 2.340454,-41.494263 4.349976,-45.832886c2.003479,-4.333313 8.788452,-6.253723 17.394043,-7.430176c9.030945,-1.236389 44.079224,-0.47052 52.519226,0.618286c8.014221,1.034119 13.049805,2.166687 14.385559,12.698792c1.335815,10.526978 1.672668,46.757507 0.667908,61.622925c-0.715027,10.581299 -3.68219,22.6073 -2.009521,23.225464c1.672668,0.618164 30.775146,-25.703918 46.833374,-36.230652c16.058228,-10.532349 43.488098,-32.515869 46.833374,-39.327393c3.345337,-6.811707 3.68219,-7.74176 5.685852,-1.548401c2.003479,6.193481 1.672363,12.387024 -1.004944,16.720093c-2.677429,4.333252 -4.013,9.914001 -1.341614,12.387024c1.004944,0.930054 9.031128,-5.263306 11.040466,-8.983826c2.009399,-3.715088 0,-10.838623 1.341736,-13.935242c1.341614,-3.096802 6.577942,-12.080688 6.353638,-19.510681c-1.335876,-44.284241 -2.961121,-110.552246 -3.345398,-255.480347c-0.407776,-155.088196 1.253113,-201.418945 22.411865,-284.281097c19.208618,-75.218842 33.641724,-90.095245 41.029541,-100.944687c6.832336,-10.028824 22.252502,-17.819885 28.706299,-17.956787c0,0 0,-0.010818 0,-0.010818c0.023865,0 0.059204,0.005432 0.094543,0.005432c0.023682,0 0.059021,-0.005432 0.094666,-0.005432c0,0 0,0.010818 0,0.010818c6.448059,0.136902 21.862366,7.933395 28.694519,17.956787c7.388123,10.843872 21.815186,25.725845 41.029297,100.944687c21.159302,82.862152 22.819824,129.192902 22.412109,284.281097c-0.378418,144.928101 -2.009277,211.201416 -3.345337,255.480347c-0.218506,7.429993 5.023682,16.413879 6.359619,19.510681c1.335815,3.096619 -0.667847,10.220154 1.341675,13.935242c2.00354,3.715088 10.029785,9.913879 11.034546,8.983826c2.67749,-2.478455 1.341675,-8.053772 -1.341919,-12.387024c-2.682983,-4.333069 -3.014038,-10.526611 -1.004395,-16.720093c2.009399,-6.193359 2.340332,-5.263306 5.691284,1.548401c3.339478,6.811523 30.775391,28.800781 46.833618,39.327393c16.05835,10.526733 45.160522,36.848816 46.833252,36.230652c1.672485,-0.618164 -1.294678,-12.644165 -2.009399,-23.225464c-1.004883,-14.865417 -0.668091,-51.095947 0.667725,-61.622925c1.335571,-10.526672 6.371338,-11.659241 14.385742,-12.698792c8.445801,-1.094177 43.488281,-1.854675 52.525146,-0.618286c8.599365,1.181885 15.390381,3.096863 17.394043,7.430176c2.009399,4.333069 3.682007,18.580261 4.349854,45.832886c0.661987,27.252075 -3.19165,57.469849 -4.68689,61.62793c-2.009277,5.575317 -9.36792,7.742065 -9.36792,7.742065c0,0 -1.004761,3.714722 -2.340332,12.386841c-1.341797,8.671936 -1.341797,14.865417 -1.341797,14.865417c0,0 144.513184,107.460693 266.608398,195.093994c122.302368,87.781006 192.89502,143.812134 192.89502,143.812134c0,0 -1.418335,5.744751 -0.135742,6.789673c2.20459,1.811035 6.377319,3.852051 6.105469,7.019897c-0.230591,2.724365 1.105347,6.581787 1.105347,6.581787c0,0 18.841797,25.720215 19.326538,31.35022c0.73877,8.824951 -6.536743,6.735229 -6.536743,6.735229zm-1463.317642,-65.151733l-14.143494,10.03418l1.223557,1.477417l14.267349,-10.12207l-1.347412,-1.389526zm28.363449,-20.123291l-26.767807,18.990723l1.341614,1.389771l26.655563,-18.914307l-1.22937,-1.466187zm28.422821,-20.167114l-26.826935,19.034302l1.22937,1.471802l26.661285,-18.91394l-1.063721,-1.592163zm35.810577,-25.403076l-34.208954,24.270508l1.063965,1.592285l34.326996,-24.358154l-1.182007,-1.504639zm33.020996,-23.422607l-31.431244,22.289917l1.182251,1.504761l31.460541,-22.322754l-1.211548,-1.471924zm33.381378,-23.679565l-31.785736,22.547363l1.217682,1.482666l31.726563,-22.509033l-1.158508,-1.520996zm34.516479,-24.494873l-32.926575,23.362427l1.158386,1.515503l33.139252,-23.510132l-1.371063,-1.367798zm34.835144,-24.713623l-33.233521,23.581299l1.365234,1.367554l33.008972,-23.416748l-1.140686,-1.532104zm34.545746,-24.505737l-32.949799,23.373169l1.140564,1.531982l32.837921,-23.30188l-1.028687,-1.603271zm34.291901,-24.330811l-32.690155,23.187378l1.028442,1.613892l32.808197,-23.274658l-1.146484,-1.526611zm32.967529,-23.384033l-31.371826,22.246094l1.146545,1.526733l31.425232,-22.290039l-1.199951,-1.482788zm32.932495,-23.362366l-31.33667,22.229919l1.205811,1.482544l31.1651,-22.109009l-1.034241,-1.603455zm38.801208,-27.536682l-37.199646,26.393372l1.034363,1.608643l37.028015,-26.267639l-0.862732,-1.734375zm10.14801,-7.19458l-8.510864,6.040222l0.857056,1.728821l8.445801,-5.996338l-0.791992,-1.772705zm31.785522,-22.546997l-30.130615,21.376221l0.792053,1.778137l30.562012,-21.682678l-1.22345,-1.47168zm531.798706,0l-1.223633,1.477112l30.568115,21.682678l0.786133,-1.778259l-30.130615,-21.381531zm31.779541,22.546997l-0.786133,1.778137l8.451782,5.996338l0.857056,-1.728821l-8.522705,-6.045654zm10.154053,7.19458l-0.862793,1.734375l37.034058,26.267639l1.028198,-1.608643l-37.199463,-26.393372zm38.801147,27.536682l-1.03418,1.608948l31.165039,22.109253l1.205811,-1.48291l-31.33667,-22.235291zm32.932373,23.362366l-1.205811,1.482788l31.425049,22.290039l1.140869,-1.526733l-31.360107,-22.246094zm32.967773,23.384033l-1.146484,1.526611l32.807739,23.274658l1.028564,-1.613892l-32.689819,-23.187378zm34.285767,24.330811l-1.028198,1.608643l32.843384,23.30188l1.140869,-1.53186l-32.956055,-23.378662zm34.551636,24.505737l-1.140625,1.526611l33.00293,23.416748l1.371582,-1.367798l-33.233887,-23.575562zm34.835205,24.713623l-1.36499,1.367798l33.13916,23.510132l1.152222,-1.515503l-32.926392,-23.362427zm34.516235,24.494873l-1.152466,1.515503l31.726563,22.508789l1.211914,-1.482666l-31.786011,-22.541626zm33.381714,23.679565l-1.211548,1.477295l31.460327,22.322754l1.17627,-1.504639l-31.425049,-22.29541zm33.020996,23.422607l-1.176514,1.499268l34.333374,24.358032l1.063843,-1.592163l-34.220703,-24.265137zm35.810303,25.403076l-1.063477,1.586426l26.661377,18.914307l1.229126,-1.471558l-26.827026,-19.029175zm28.423096,20.167114l-1.229614,1.466187l26.65564,18.914307l1.347656,-1.389771l-26.773682,-18.990723zm28.369385,20.123291l-1.347656,1.389526l14.2677,10.12207l1.217407,-1.477417l-14.137451,-10.03418z");
		
		return b.getIcon();
	}
}
