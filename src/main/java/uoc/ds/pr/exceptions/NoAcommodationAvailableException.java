package uoc.ds.pr.exceptions;

import uoc.ds.pr.ShippingLine;

public class NoAcommodationAvailableException extends DSException {

    public NoAcommodationAvailableException(ShippingLine.AccommodationType accommodationType) {
        super("No accommodation available for accommodation type " + accommodationType);
    }
}
