package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.lang.annotation.TextArea;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoDuplicate("nombre")
public @Data
class UserProfile extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private long id;
    @Lob
    @NoEmpty
    private String nombre;
    @Lob
    @TextArea
    @NoEmpty
    private String descripcion;
    private boolean pordefectorgistro;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "userprofile_provideraccess", joinColumns = @JoinColumn(name = "iduserprofile"), inverseJoinColumns = @JoinColumn(name = "idprovideraccess"))
    private List<ProviderAccess> providersaccess;

    public boolean contain(String menuprovider) {
        for (ProviderAccess p : providersaccess) {
            if (p.getMenuprovider().equalsIgnoreCase(menuprovider))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
