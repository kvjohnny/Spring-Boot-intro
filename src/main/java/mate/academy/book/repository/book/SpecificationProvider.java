package mate.academy.book.repository.book;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    Specification<T> getSpecification(String fieldName, String param);
}
