package eu.interopehrate.hcpapp.converters.entity.utils;

import eu.interopehrate.hcpapp.jpa.entities.administration.AddressEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public abstract class AddressUtils {
    public static String lastAddress(Collection<AddressEntity> addressCollection) {
        if (CollectionUtils.isEmpty(addressCollection)) {
            return "";
        } else {
            List<AddressEntity> addressList = new ArrayList<>(addressCollection);
            addressList.sort(Comparator.comparing(AddressEntity::getCreatedDate).reversed());
            AddressEntity ae = addressList.get(0);
            return String.join(", ",
                    ae.getCity().getCountry().getName(),
                    ae.getCity().getName(),
                    ae.getStreet(),
                    ae.getNumber(),
                    ae.getDetails(),
                    ae.getPostalCode()
            );
        }
    }
}
