/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.utils;

import java.util.LinkedList;

public class Signal<T> {

	private LinkedList<Listener<T>> listeners = new LinkedList<Signal.Listener<T>>();
	
	private boolean canceled;
	
	private boolean stackMode;
	
	public Signal() {
		this( false );
	}
	
	public Signal( boolean stackMode ) {
		this.stackMode = stackMode;
	}
	
	public synchronized void add( Listener<T> listener ) {
		if (!listeners.contains( listener )) {
			if (stackMode) {
				listeners.addFirst( listener );
			} else {
				listeners.addLast( listener );
			}
		}
	}
	
	public synchronized void remove( Listener<T> listener ) {
		listeners.remove( listener );
	}
	
	public synchronized void removeAll() {
		listeners.clear();
	}
	
	public synchronized void replace( Listener<T> listener ) {
		removeAll();
		add( listener );
	}
	
	public synchronized int numListeners() {
		return listeners.size();
	}
	
	public synchronized void dispatch( T t ) {

		@SuppressWarnings("unchecked")
		Listener<T>[] list = listeners.toArray( new Listener[0] );

		canceled = false;
		for (Listener<T> listener : list) {

			if (listeners.contains(listener)) {
				listener.onSignal(t);
				if (canceled) {
					return;
				}
			}

		}
	}
	
	public void cancel() {
		canceled = true;
	}
	
	public interface Listener<T> {
		void onSignal(T t);
	}
}
