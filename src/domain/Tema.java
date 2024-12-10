package domain;

import java.util.ArrayList;

public class Tema {
	
	public enum Theme {
        CLARO, OSCURO
    }
	private static Theme temaActual = Theme.CLARO; 
    private static final ArrayList<CambiarTema> listeners = new ArrayList<>();

    public static void setTema(Theme tema) {
    	temaActual = tema;
        notifyListeners();
    }

    public static Theme getTemaActual() {
        return temaActual;
    }

    public static void addListener(CambiarTema listener) {
        listeners.add(listener);
    }

    private static void notifyListeners() {
        for (CambiarTema listener : listeners) {
            listener.onThemeChanged(temaActual);
        }
    }

    public interface CambiarTema {
        void onThemeChanged(Theme theme);
    }
}


