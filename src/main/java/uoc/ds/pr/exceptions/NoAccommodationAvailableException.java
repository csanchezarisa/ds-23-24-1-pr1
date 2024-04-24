package uoc.ds.pr.exceptions;

import uoc.ds.pr.ShippingLine;

public class NoAccommodationAvailableException extends DSException {

    public NoAccommodationAvailableException(ShippingLine.AccommodationType accommodationType) {
        super("No accommodation available for accommodation type " + accommodationType);
    }
}
