/*
 * Copyright 2015 Seth Traverse
 *
 * This file is part of ViaWareDLNAServer.
 *
 * ViaWareDLNAServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ViaWareDLNAServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ViaWareDLNAServer. If not, see <http://www.gnu.org/licenses/>.
 */

package ca.viaware.dlna.upnp.service.base;

import ca.viaware.api.logging.Log;

import java.util.HashMap;

public abstract class Action {

    private ActionArgument[] in;
    private ActionArgument[] out;

    public Action() {
        this.in = new ActionArgument[0];
        this.out = new ActionArgument[0];
    }

    public Action(ActionArgument[] in, ActionArgument[] out) {
        this.in = in;
        this.out = out;
    }

    public ActionArgument[] getIn() {
        return in;
    }

    public ActionArgument[] getOut() {
        return out;
    }

    public boolean hasArgument(String arg) {
        return getStateVarFor(arg) != null;
    }

    public String getStateVarFor(String arg) {
        for (ActionArgument a : getIn()) {
            if (a.getName().equals(arg)) return a.getStateVariable();
        }
        for (ActionArgument a : getOut()) {
            if (a.getName().equals(arg)) return a.getStateVariable();
        }
        return null;
    }

    public Result run(HashMap<String, Object> parameters) {
        return handle(parameters);
    }

    protected abstract Result handle(HashMap<String, Object> parameters);

}
