package com.champlain.music.playlistsubdomain.buisneelogiclayer;

import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.Playlist;
import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.PlaylistIdentifier;
import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.PlaylistRepository;
import com.champlain.music.playlistsubdomain.datamapperlayer.PlaylistRequestModelMapper;
import com.champlain.music.playlistsubdomain.datamapperlayer.PlaylistResponseModelMapper;
import com.champlain.music.playlistsubdomain.presentationlayer.ArtistResponseModel;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistRequestModel;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistResponseModel;
import com.champlain.music.playlistsubdomain.presentationlayer.SongResponseModel;
import com.champlain.music.utils.NotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistRequestModelMapper playlistRequestModelMapper;
    private final PlaylistResponseModelMapper playlistResponseModelMapper;
    private final UserServiceClient userServiceClient;
    private final SongServiceClient songServiceClient;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository, PlaylistRequestModelMapper playlistRequestModelMapper, PlaylistResponseModelMapper playlistResponseModelMapper, UserServiceClient userServiceClient, SongServiceClient songServiceClient) {
        this.playlistRepository = playlistRepository;
        this.playlistRequestModelMapper = playlistRequestModelMapper;
        this.playlistResponseModelMapper = playlistResponseModelMapper;
        this.userServiceClient = userServiceClient;
        this.songServiceClient = songServiceClient;
    }

    @Override
    public List<PlaylistResponseModel> getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<PlaylistResponseModel> playlistResponseModels = playlistResponseModelMapper.entityToResponseModelList(playlists);
        playlistResponseModels.forEach(playlist -> playlist.setSongs(playlists.stream()
                .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                .findAny()
                .map(p -> p.getSongs().stream().map(songServiceClient::getSongById).toList())
                .orElseGet(List::of)));
        playlistResponseModels.forEach(playlist -> playlist.setUser(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> userServiceClient.getUserById(p.getUser()))
                        .orElse(null)
        ));
        return playlistResponseModels;
    }

    @Override
    public PlaylistResponseModel getPlaylistById(String playlistId) {
        Playlist playlist = playlistRepository.findByIdentifier_PlaylistId(playlistId);
        PlaylistResponseModel playlistResponseModel = playlistResponseModelMapper.entityToResponseModel(playlist);
        playlistResponseModel.setSongs(playlist.getSongs().stream().map(songServiceClient::getSongById).toList());
        return playlistResponseModel;
    }

    @Override
    public PlaylistResponseModel addPlaylist(PlaylistRequestModel playlistRequestModel) {
        Playlist playlist = playlistRequestModelMapper.requestModelToEntity(playlistRequestModel);
        Time duration = new Time(0);
        for (String songIdentifier : playlist.getSongs()) {
            if(songServiceClient.getSongById(songIdentifier) == null)
                throw new NotFoundException("song with id " + songIdentifier + " was not found");
            else
                duration.setTime(duration.getTime() + playlist.getDuration().getTime());
        }
        if(userServiceClient.getUserById(playlist.getUser()) == null)
            throw new NotFoundException("user with id " + playlist.getUser() + " was not found");

        playlist.setDuration(duration);
        playlist.setIdentifier(new PlaylistIdentifier());
        return playlistResponseModelMapper.entityToResponseModel(playlistRepository.save(playlist));
    }

    @Override
    public PlaylistResponseModel updatePlaylist(PlaylistRequestModel playlistRequestModel, String playlistId) {
        Playlist oldPlaylist = playlistRepository.findByIdentifier_PlaylistId(playlistId);
        if(oldPlaylist != null) {
            Playlist playlist = playlistRequestModelMapper.requestModelToEntity(playlistRequestModel);
            Time duration = new Time(0);
            for (String songIdentifier : playlist.getSongs()) {
                if(songServiceClient.getSongById(songIdentifier) == null)
                    throw new NotFoundException("song with id " + songIdentifier + " was not found");
                else
                    duration.setTime(duration.getTime() + playlist.getDuration().getTime());
            }
            if(userServiceClient.getUserById(playlist.getUser()) == null)
                throw new NotFoundException("user with id " + playlist.getUser() + " was not found");

            playlist.setIdentifier(new PlaylistIdentifier(playlistId));
            playlist.setDuration(duration);
            playlist.setId(oldPlaylist.getId());
            return playlistResponseModelMapper.entityToResponseModel(playlistRepository.save(playlist));
        }
        else
            throw new NotFoundException("playlist with id " + playlistId + " was not found");
    }

    @Override
    public void deletePlaylist(String playlistId) {
        Playlist playlist = playlistRepository.findByIdentifier_PlaylistId(playlistId);
        if (playlist != null) {
            playlistRepository.delete(playlist);
        }
        else
            throw new NotFoundException("playlist with id " + playlistId + " was not found");
    }

    @Override
    public List<ArtistResponseModel> getAllArtists(String playlistId) {
        return getPlaylistById(playlistId).getSongs().stream().map(SongResponseModel::getArtists).flatMap(List::stream).distinct().toList();
    }
}
