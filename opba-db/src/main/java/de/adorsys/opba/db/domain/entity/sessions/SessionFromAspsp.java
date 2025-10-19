package de.adorsys.opba.db.domain.entity.sessions;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class SessionFromAspsp {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String authId;

        private String cookie; // par exemple JSESSIONID=xxx; XSRF-TOKEN=yyy

        private String xsrfToken;

}
