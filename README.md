## PR1

## Author
- Cristóbal Sánchez Arisa
- csanchezarisa@uoc.edu

## Changes
- Class `DSArray` implemented. Used as a static vector, because there's no static vectors on `DSLib`.
- Class `DSLinkedList` implemented. Used as a linked list implementing some specific methods to find and check
  existence.
- Class `FiniteLinkedList` implemented. Used as a linked list but with a maximum number of elements.
- Class `OrderedVector` implemented. Used as a finite linked list but ordered.
- `NoAccommodationAvailableException` renamed to `NoAccommodationAvailableException`, in `ShippingLine` and test
  classes.
- New tests added: `DsArrayTest`, `DsLinkedListTest` and `ShippingLinePR1TestV2`.