package uoc.ds.pr;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.LandingNotDoneException;
import uoc.ds.pr.exceptions.VehicleNotFoundException;
import uoc.ds.pr.exceptions.VoyageNotFoundException;
import uoc.ds.pr.util.DateUtils;

public class ShippingLinePR1TestV2 {

    private ShippingLine theShippingLine;

    @Before
    public void setUp() throws Exception {
        this.theShippingLine = FactoryShippingLine.getShippingLine();
        Assert.assertEquals(7, this.theShippingLine.numShips());
        Assert.assertEquals(9, this.theShippingLine.numRoutes());
        Assert.assertEquals(18, this.theShippingLine.numClients());
        Assert.assertEquals(3, this.theShippingLine.numVoyages());
    }

    @After
    public void tearDown() {
        this.theShippingLine = null;
    }

    @Test
    public void unloadTimeTest() throws Exception {
        Assert.assertThrows(VoyageNotFoundException.class,
                () -> theShippingLine.unloadTime("vehicleId1", "voyageIdXXXX"));

        // Add reserved
        String[] clientsA = {"clientId1", "clientId2", "clientId3", "clientId4", "clientId5", "clientId6"};
        theShippingLine.reserve(clientsA, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR, "2251VV", 200);

        String[] clientsB = {"clientId7", "clientId8"};
        theShippingLine.reserve(clientsB, "voyageId1", ShippingLine.AccommodationType.CABIN2, "B1923AT", 200);

        String[] clientsC = {"clientId9", "clientId10", "clientId11", "clientId12"};
        theShippingLine.reserve(clientsC, "voyageId1", ShippingLine.AccommodationType.CABIN4, null, 200);

        // Load some reserved
        theShippingLine.load("clientId7", "voyageId1", DateUtils.createDate("30-07-2024 09:30:00"));
        theShippingLine.load("clientId9", "voyageId1", DateUtils.createDate("30-07-2024 09:30:00"));
        theShippingLine.load("clientId10", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));
        theShippingLine.load("clientId8", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));

        Assert.assertThrows(LandingNotDoneException.class,
                () -> theShippingLine.unloadTime("vehicleId1", "voyageId1"));

        theShippingLine.load("clientId1", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));
        theShippingLine.load("clientId2", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));

        // Unload
        theShippingLine.unload("voyageId1");

        Assert.assertThrows(VehicleNotFoundException.class,
                () -> theShippingLine.unloadTime("vehicleId1", "voyageId1"));

        Assert.assertEquals(7, theShippingLine.unloadTime("2251VV", "voyageId1"));
        Assert.assertEquals(14, theShippingLine.unloadTime("B1923AT", "voyageId1"));
    }
}