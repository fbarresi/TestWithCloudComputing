package de.othr.cc.rest;

import de.othr.cc.rest.entity.Student;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import javassist.tools.rmi.ObjectNotFoundException;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import static org.dizitart.no2.objects.filters.ObjectFilters.*;

@Path("stv")
public class StudentService {

    private static Nitrite db = null;
    private final ObjectRepository<Student> repository;
    public StudentService(){
        if(db == null) {
            db = Nitrite.builder()
                    .filePath("test.db")
                    .openOrCreate();
        }
        repository = db.getRepository(Student.class);
    }

    @Path("students/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudentById(@PathParam("id") int id)
    {
        return repository.find(eq("id", id)).firstOrDefault();
    }

    @Path("students")
    @GET
    public Student[] getStudentByRange(@QueryParam("from") int from, @QueryParam("to") int to)
    {
        if(to < from) throw new IllegalArgumentException("Invalid Range");
        if(to == 0)
            return repository.find(gte("id", from)).toList().toArray(new Student[0]);
        return repository.find(and(gte("id", from), lte("id", to))).toList().toArray(new Student[0]);
    }

    @Path("students")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Student addStudent(Student student)
    {
        var size = repository.getDocumentCollection().size();
        student.id = (int)size+1;
        repository.insert(student);
        return student;
    }

    @Path("students/{id}")
    @DELETE
    public void deleteStudentbyId(@PathParam("id") int id) throws ObjectNotFoundException {
        var s = getStudentById(id);
        if(s != null) {
            repository.remove(s);
            return;
        }
        throw new ObjectNotFoundException("Student with id "+id);
    }
}
