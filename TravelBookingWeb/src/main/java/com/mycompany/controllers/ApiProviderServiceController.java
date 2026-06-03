
package com.mycompany.controllers;

import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.ProviderProfiles;
import com.mycompany.pojo.Users;
import com.mycompany.services.CloudinaryService;
import com.mycompany.services.HotelRoomService;
import com.mycompany.services.ProviderService;
import com.mycompany.services.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ApiProviderServiceController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private HotelRoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        Users user = this.userService.getUserByUsername(principal.getName());
        ProviderProfiles provider = this.providerService.getProviderByUserId(user.getId());

        if (provider == null) {
            return new ResponseEntity<>("Provider profile not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(provider, HttpStatus.OK);
    }

    @GetMapping("/services")
    public ResponseEntity<?> list(@RequestParam(value = "providerId") Long providerId) {
        return new ResponseEntity<>(this.providerService.getProviderServices(providerId), HttpStatus.OK);
    }

    @PostMapping("/services")
    public ResponseEntity<?> create(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.providerService.addOrUpdateService(null, params), HttpStatus.CREATED);
    }

    @PostMapping(value = "/services", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMultipart(@RequestParam Map<String, String> params,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {
        Map<String, String> normalizedParams = new HashMap<>(params);
        String thumbnail = this.cloudinaryService.upload(thumbnailFile, "travel/services");
        if (thumbnail != null) {
            normalizedParams.put("thumbnail", thumbnail);
        }

        return new ResponseEntity<>(this.providerService.addOrUpdateService(null, normalizedParams), HttpStatus.CREATED);
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Long id, @RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.providerService.addOrUpdateService(id, params), HttpStatus.OK);
    }

    @PutMapping(value = "/services/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMultipart(@PathVariable(value = "id") Long id,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {
        Map<String, String> normalizedParams = new HashMap<>(params);
        String thumbnail = this.cloudinaryService.upload(thumbnailFile, "travel/services");
        if (thumbnail != null) {
            normalizedParams.put("thumbnail", thumbnail);
        }

        return new ResponseEntity<>(this.providerService.addOrUpdateService(id, normalizedParams), HttpStatus.OK);
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id, @RequestParam(value = "providerId") Long providerId) {
        try {
            this.providerService.deleteService(id, providerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Không thể xóa dịch vụ vì đã có dữ liệu đặt dịch vụ liên quan.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> rooms(@RequestParam(value = "hotelId") Long hotelId) {
        return new ResponseEntity<>(this.roomService.getRoomsByHotelId(hotelId), HttpStatus.OK);
    }

    @PostMapping("/rooms")
    public ResponseEntity<HotelRooms> createRoom(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.roomService.addOrUpdateRoom(null, params), HttpStatus.CREATED);
    }

    @PostMapping(value = "/rooms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HotelRooms> createRoomMultipart(@RequestParam Map<String, String> params,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Map<String, String> normalizedParams = new HashMap<>(params);
        String image = this.cloudinaryService.upload(imageFile, "travel/rooms");
        if (image != null) {
            normalizedParams.put("image", image);
        }

        return new ResponseEntity<>(this.roomService.addOrUpdateRoom(null, normalizedParams), HttpStatus.CREATED);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<HotelRooms> updateRoom(@PathVariable(value = "id") Long id, @RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.roomService.addOrUpdateRoom(id, params), HttpStatus.OK);
    }

    @PutMapping(value = "/rooms/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HotelRooms> updateRoomMultipart(@PathVariable(value = "id") Long id,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Map<String, String> normalizedParams = new HashMap<>(params);
        String image = this.cloudinaryService.upload(imageFile, "travel/rooms");
        if (image != null) {
            normalizedParams.put("image", image);
        }

        return new ResponseEntity<>(this.roomService.addOrUpdateRoom(id, normalizedParams), HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable(value = "id") Long id) {
        this.roomService.deleteRoom(id);
    }
}
