package kz.jusan.timka.solidbank.repositories;


import kz.jusan.timka.solidbank.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}