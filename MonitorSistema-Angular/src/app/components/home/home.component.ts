import { Component, OnInit } from '@angular/core';
import { Disk } from 'src/app/models/disk.model';
import { Files } from 'src/app/models/file.model';
import { DriveService } from 'src/app/services/Drives.services';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  listDrives: string[] | any ;
  listFiles: Files[] =[] ;

  disk=new Disk();
  public labels = ['usado', 'libre', 'dañado'];
  public data = [0, 0, 0];

  constructor(
    private _sDriveService: DriveService
  ) { }

  ngOnInit(): void {
  this.load();



  }

  load()
  {
    this._sDriveService.getDrives()
    .subscribe(
      res => {
        console.log(res.body);
        this.listDrives=res.body;
      },
      err => {
        console.log(err);
      }
    );
  }

  info()
  {
    this._sDriveService.getDriveInfo(this.disk.label)
    .subscribe(
      res => {
        console.log(res.body);
        this.disk.SpaceUsed=this.getGigas(res.body.spaceUsed)
        this.disk.SpaceTotal=this.getGigas(res.body.spaceTotal)
        this.disk.SpaceFree=this.getGigas(res.body.spaceFree)
        this.disk.SpaceUnUsable=this.getGigas((Number(res.body.spaceTotal)-(Number(res.body.spaceUsed)+Number(res.body.spaceFree))))
        this.data=[ Number(this.disk.SpaceUsed)*100/Number(this.disk.SpaceTotal),
                    Number(this.disk.SpaceFree)*100/Number(this.disk.SpaceTotal),
                    Number(this.disk.SpaceUnUsable)*100/Number(this.disk.SpaceTotal)]
        console.log(this.data)

      },
      err => {
        console.log(err);
      }
    );
  }

  files()
  {
    this._sDriveService.getFilesList(this.disk.label,"")
    .subscribe( (resp) =>
    {
      this.listFiles = resp.body as Files[];
    });
  }




  getGigas(bytes:any)
  {
    return String(Number(bytes) / 1073741824)
  }

  getMegas(bytes:any)
  {
    return String(Number(bytes) / 1073741824)
  }

}
