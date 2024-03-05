package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kodnest.tunehub.entity.Playlist;
import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.service.PlaylistService;
import com.kodnest.tunehub.service.SongService;

@Controller
public class PlaylistController {
	@Autowired
	SongService songService;
	
	@Autowired
	PlaylistService playlistService;
	
	@GetMapping("/createplaylist")
	public String createplaylist(Model model) {
		List<Song> songList = songService.fetchAllSong();
		model.addAttribute("songs", songList);
		return "createplaylist";
	}
	
	@GetMapping("/viewplaylist")
	public String viewplaylist(Model model) {
		List<Playlist> allplaylist = playlistService.fetchAllPlaylist();
		model.addAttribute("allplaylist",allplaylist);
		return "displayplaylist";
	}
	
	
	
    @PostMapping("/addplaylist")
	public String addplaylist(@ModelAttribute Playlist playlist) {
    	
    	//updating the playlist table
	playlistService.addplaylist(playlist);
	
	//updating the song table
	List<Song> songList = playlist.getSongs();
	for(Song s: songList) {
		s.getPlaylists().add(playlist);
		songService.updateSong(s);
	}
	return "adminhome";
	
	}
    
    @GetMapping("/viewplaylists")
    public String viewplaylists(Model model) {
        List<Playlist> allplaylist = playlistService.fetchAllPlaylist();
        model.addAttribute("allplaylist", allplaylist);
        return "displayplaylist";
    }
}
