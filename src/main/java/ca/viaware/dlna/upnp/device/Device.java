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

package ca.viaware.dlna.upnp.device;

import ca.viaware.dlna.upnp.service.Service;

import java.util.ArrayList;

public abstract class Device {

    private ArrayList<Service<? extends Device>> services;

    public Device() {
        services = new ArrayList<Service<? extends Device>>();
    }

    protected void addService(Service<? extends Device> s) {
        services.add(s);
    }

    public ArrayList<Service<? extends Device>> getServices() {
        return services;
    }

    public abstract String getType();
    public abstract String getUid();
    public abstract int getVersion();
    public abstract String getName();
    public abstract String getManufacturer();
    public abstract String getWebsite();
    public abstract String getModelNumber();

}
