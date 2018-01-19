//
//  HomeViewController.m
//  WebViewChatWindow
//
//  Created by Allon on 5/27/15.
//  Copyright (c) 2015 Allon. All rights reserved.
//

#import <visitorClient/VisitorClientController.h>
#import "HomeViewController.h"

@implementation HomeViewController

-(void) viewDidLoad{
    [self setTitle:@"Comm100 Live Chat Client Demo"];
}


-(IBAction)startChat:(id)sender{
    NSString * chatUrl =
        //[NSString stringWithFormat:@"https://bde98329.ngrok.io?v=%f", [[NSDate date] timeIntervalSince1970]];
        [NSString stringWithFormat:@"%@&v=%f", txtServerUrl.text, [[NSDate date] timeIntervalSince1970]];

    VisitorClientController * visitorClient = [[VisitorClientController alloc] initWithChatUrl:chatUrl];
    [self.navigationController pushViewController:visitorClient animated:YES];
}

@end
