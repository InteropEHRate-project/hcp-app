package eu.interopehrate.hcpapp.converters.utils;

import eu.interopehrate.hcpapp.jpa.entities.AddressEntity;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

public abstract class AddressUtils {
    public static String lastAddress(List<AddressEntity> addressList) {
        if (CollectionUtils.isEmpty(addressList)) {
            return "";
        } else {
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
